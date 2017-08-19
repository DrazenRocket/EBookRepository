package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.repository.ICategoryRepository;
import com.drazendjanic.ebookrepository.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

}
