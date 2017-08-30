package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.EBook;

import java.util.List;

public interface IEBookService {

    List<EBook> findAllEBooks();

    List<EBook> findAllEBooksByCategoryId(Long id);

}
