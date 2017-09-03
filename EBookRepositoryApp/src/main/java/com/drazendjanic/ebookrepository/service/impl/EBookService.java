package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.repository.IEBookRepository;
import com.drazendjanic.ebookrepository.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${e-book-repository.file-repository.base-path}")
    private String repositoryPath;

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
        String fileName = repositoryPath + eBook.getFilename();
        File file = new File(fileName);

        if (file.exists() && !file.isDirectory()) {
            savedEBook = eBookRepository.save(eBook);
        }
        else {
            throw new IllegalArgumentException("File name is not valid.");
        }

        return savedEBook;
    }

    @Override
    @Transactional
    public File createEBookFile() throws IOException {
        File repository = new File(repositoryPath);
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
            String fileName = repositoryPath + eBook.getFilename();
            File file = new File(fileName);
            Path filePath = Paths.get(file.getAbsolutePath());

            loadedFile = new ByteArrayResource(Files.readAllBytes(filePath));
        }
        else {
            throw new NotFoundException("E-Book does not exists.");
        }

        return loadedFile;
    }

}
