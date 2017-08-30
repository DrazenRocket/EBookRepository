package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.repository.IEBookRepository;
import com.drazendjanic.ebookrepository.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EBookService implements IEBookService {

    @Autowired
    private IEBookRepository eBookRepository;

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

}
