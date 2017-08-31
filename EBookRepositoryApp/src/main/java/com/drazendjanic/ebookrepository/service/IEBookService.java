package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface IEBookService {

    EBook findEBookById(Long id);

    List<EBook> findAllEBooks();

    List<EBook> findAllEBooksByCategoryId(Long id);

    Resource loadEBookFileByEBookId(Long id) throws NotFoundException, IOException;

}
