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
import com.drazendjanic.ebookrepository.ir.searcher.query.QueryOperator;
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
        List<HitEBookDto> hitEBookDtos = null;
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

        hitEBookDtos = transformHitEBookToHitEBookDto(hitEBooks);

        return hitEBookDtos;
    }

    @Override
    @Transactional
    public List<HitEBookDto> searchEBooks(MultiFieldSearchDto multiFieldSearchDto) {
        List<HitEBookDto> hitEBookDtos = null;
        List<HitEBook> hitEBooks = null;
        InformationRetriever informationRetriever = new InformationRetriever(rawDataRepositoryPath, indexedDataRepositoryPath);
        List<String> requiredHighlights = new ArrayList<String>();
        QueryType queryType = QueryType.valueOf(multiFieldSearchDto.getQueryType());
        QueryOperator queryOperator = QueryOperator.valueOf(multiFieldSearchDto.getQueryOperator());
        BooleanClause.Occur occur = queryOperator.equals(QueryOperator.AND) ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD;
        BooleanQuery booleanQuery = new BooleanQuery();

        try {
            if (multiFieldSearchDto.getTitle() != null && !multiFieldSearchDto.getTitle().trim().isEmpty()) {
                requiredHighlights.add("title");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "title", multiFieldSearchDto.getTitle().trim()), occur);
            }
            if (multiFieldSearchDto.getAuthor() != null && !multiFieldSearchDto.getAuthor().trim().isEmpty()) {
                requiredHighlights.add("author");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "author", multiFieldSearchDto.getAuthor().trim()), occur);
            }
            if (multiFieldSearchDto.getKeywords() != null && !multiFieldSearchDto.getKeywords().trim().isEmpty()) {
                requiredHighlights.add("keywords");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "keywords", multiFieldSearchDto.getKeywords().trim()), occur);
            }
            if (multiFieldSearchDto.getContent() != null && !multiFieldSearchDto.getContent().trim().isEmpty()) {
                requiredHighlights.add("textContent");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "textContent", multiFieldSearchDto.getContent().trim()), occur);
            }
            if (multiFieldSearchDto.getLanguage() != null && !multiFieldSearchDto.getLanguage().trim().isEmpty()) {
                requiredHighlights.add("language");
                booleanQuery.add(QueryBuilder.buildQuery(queryType, "language", multiFieldSearchDto.getLanguage().trim()), occur);
            }

            hitEBooks = informationRetriever.getEBooks(booleanQuery, requiredHighlights);
            hitEBookDtos = transformHitEBookToHitEBookDto(hitEBooks);
        }catch (ParseException e) {
            hitEBookDtos = new ArrayList<HitEBookDto>();
        }

        return hitEBookDtos;
    }

    @Override
    @Transactional
    public List<HitEBookDto> transformHitEBookToHitEBookDto(List<HitEBook> hitEBooks) {
        List<HitEBookDto> hitEBookDtos = new ArrayList<HitEBookDto>();

        if (hitEBooks != null) {
            for (HitEBook hitEBook : hitEBooks) {
                EBook eBook = findEBookById(hitEBook.getId());
                HitEBookDto hitEBookDto = HitEBookAssembler.toHitEBookDto(hitEBook, eBook);

                hitEBookDtos.add(hitEBookDto);
            }
        }

        return hitEBookDtos;
    }

}
