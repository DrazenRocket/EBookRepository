package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.entity.Language;
import com.drazendjanic.ebookrepository.exception.IncompleteIndexDocumentException;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.ir.indexer.DocumentIndexer;
import com.drazendjanic.ebookrepository.ir.indexer.handler.DocumentHandler;
import com.drazendjanic.ebookrepository.ir.indexer.handler.PdfHandler;
import com.drazendjanic.ebookrepository.repository.IEBookRepository;
import com.drazendjanic.ebookrepository.service.IEBookService;
import com.drazendjanic.ebookrepository.service.ILanguageService;
import org.apache.lucene.document.Document;
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

}
