package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.Category;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.repository.ICategoryRepository;
import com.drazendjanic.ebookrepository.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    @Transactional
    public boolean categoryExists(Long categoryId) {
        boolean exists = categoryRepository.exists(categoryId);

        return exists;
    }

    @Override
    @Transactional
    public Category findCategoryById(Long id) {
        Category category = categoryRepository.findOne(id);

        return category;
    }

    @Override
    @Transactional
    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories;
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);

        return savedCategory;
    }

}
