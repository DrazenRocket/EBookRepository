package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.assembler.HitEBookAssembler;
import com.drazendjanic.ebookrepository.dto.HitEBookDto;
import com.drazendjanic.ebookrepository.dto.MultiFieldSearchDto;
import com.drazendjanic.ebookrepository.dto.SingleFieldSearchDto;
import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.entity.Language;
import com.drazendjanic.ebookrepository.exception.IncompleteIndexDocumentException;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.ir.indexer.DocumentIndexer;
import com.drazendjanic.ebookrepository.ir.indexer.handler.DocumentHandler;
import com.drazendjanic.ebookrepository.ir.indexer.handler.PdfHandler;
import com.drazendjanic.ebookrepository.ir.searcher.InformationRetriever;
import com.drazendjanic.ebookrepository.ir.searcher.model.HitEBook;
import com.drazendjanic.ebookrepository.ir.searcher.query.QueryBuilder;
import com.drazendjanic.ebookrepository.ir.searcher.query.QueryType;
import com.drazendjanic.ebookrepository.repository.IEBookRepository;
import com.drazendjanic.ebookrepository.service.IEBookService;
import com.drazendjanic.ebookrepository.service.ILanguageService;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class EBookService implements IEBookService {

    @Autowired
    private IEBookRepository eBookRepository;

    @Autowired
    private ILanguageService languageService;

    @Value("${e-book-repository.file-repository.raw-data-path}")
    private String rawDataRepositoryPath;

    @Value("${e-book-repository.file-repository.indexed-data-path}")
    private String indexedDataRepositoryPath;

    private DocumentHandler documentHandler;

    private DocumentIndexer documentIndexer;

    @PostConstruct
    public void init() {
        documentHandler = new PdfHandler();
        documentIndexer = new DocumentIndexer(indexedDataRepositoryPath);
    }

    @Override
    @Transactional
    public EBook findEBookById(Long id) {
        EBook eBook = eBookRepository.findOne(id);

        return eBook;
    }

    @Override
    @Transactional
    public List<EBook> findAllEBooks() {
        List<EBook> eBooks = eBookRepository.findAll();

        return eBooks;
    }

    @Override
    @Transactional
    public List<EBook> findAllEBooksByCategoryId(Long id) {
        List<EBook> eBooks = eBookRepository.findByCategoryId(id);

        return eBooks;
    }

    @Override
    @Transactional
    public EBook saveEBook(EBook eBook) {
        EBook savedEBook = null;
        String fileName = rawDataRepositoryPath + eBook.getFilename();
        File file = new File(fileName);

        if (file.exists() && !file.isDirectory()) {
            savedEBook = eBookRepository.save(eBook);
            indexEBook(eBook);
        }
        else {
            throw new IllegalArgumentException("File name is not valid.");
        }

        return savedEBook;
    }

    @Override
    @Transactional
    public File createEBookFile() throws IOException {
        File repository = new File(rawDataRepositoryPath);
        File eBookFile = File.createTempFile("ebook", ".pdf", repository);

        return eBookFile;
    }

    @Override
    @Transactional
    public File saveEBookFile(byte[] bytes) throws IOException {
        File eBookFile = createEBookFile();
        FileOutputStream fileOutputStream = new FileOutputStream(eBookFile);

        fileOutputStream.write(bytes);
        fileOutputStream.close();

        return eBookFile;
    }

    @Override
    @Transactional
    public Resource loadEBookFileByEBookId(Long id) throws NotFoundException, IOException {
        Resource loadedFile = null;
        EBook eBook = findEBookById(id);

        if (eBook != null) {
            String fileName = rawDataRepositoryPath + eBook.getFilename();
            File file = new File(fileName);
            Path filePath = Paths.get(file.getAbsolutePath());

            loadedFile = new ByteArrayResource(Files.readAllBytes(filePath));
        }
        else {
            throw new NotFoundException("E-Book does not exists.");
        }

        return loadedFile;
    }

    @Override
    @Transactional
    public void indexEBook(EBook eBook) {
        try {
            Language language = languageService.findLanguageById(eBook.getLanguage().getId());
            Document document = documentHandler.getDocument(eBook, language, rawDataRepositoryPath);

            if (eBook.getId() != null) {
                documentIndexer.deleteDocumentByIdField(eBook.getId().toString());
            }

            documentIndexer.addDocument(document);
        }
        catch (IncompleteIndexDocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public List<HitEBookDto> searchEBooks(SingleFieldSearchDto singleFieldSearchDto) {
        List<HitEBookDto> hitEBookDtos = new ArrayList<HitEBookDto>();
        List<HitEBook> hitEBooks = null;
        String fieldName = singleFieldSearchDto.getFieldName().trim();
        String fieldValue = singleFieldSearchDto.getFieldValue().trim();
        QueryType queryType = QueryType.valueOf(singleFieldSearchDto.getQueryType());

        try {
            Query query = QueryBuilder.buildQuery(queryType, fieldName, fieldValue);
            InformationRetriever informationRetriever = new InformationRetriever(rawDataRepositoryPath, indexedDataRepositoryPath);
            List<String> queriedHighlights = new ArrayList<String>();

            queriedHighlights.add(fieldName.trim());
            hitEBooks = informationRetriever.getEBooks(query, queriedHighlights);
        }
        catch (ParseException e) {
            hitEBooks = new ArrayList<HitEBook>();
        }

        for (HitEBook hitEBook : hitEBooks) {
            EBook eBook = findEBookById(hitEBook.getId());
            HitEBookDto hitEBookDto = HitEBookAssembler.toHitEBookDto(hitEBook, eBook);

            hitEBookDtos.add(hitEBookDto);
        }

        return hitEBookDtos;
    }

    @Override
    @Transactional
    public List<HitEBook> searchEBooks(MultiFieldSearchDto multiFieldSearchDto) {
        List<HitEBook> hitEBooks = null;
        InformationRetriever informationRetriever = new InformationRetriever(rawDataRepositoryPath, indexedDataRepositoryPath);
        BooleanQuery booleanQuery = new BooleanQuery();
        List<BooleanClause> booleanClauses = getBooleanClauses(multiFieldSearchDto);
        List<String> requiredHighlights = new ArrayList<>();

        for(BooleanClause booleanClause : booleanClauses) {
            booleanQuery.add(booleanClause);
        }

        hitEBooks = informationRetriever.getEBooks(booleanQuery, requiredHighlights);

        return hitEBooks;
    }

    private List<BooleanClause> getBooleanClauses(MultiFieldSearchDto multiFieldSearchDto) {
        List<BooleanClause> booleanClauses = new ArrayList<BooleanClause>();
        // TODO Prepraviti metodu da prihvata niz stringova koji predstavljaju polja, tip query-a i da li je u pitanju and ili or
        // TODO Napraviti metodu za dobijanje niza stringova
        return booleanClauses;
    }

}
