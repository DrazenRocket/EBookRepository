package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.Category;
import com.drazendjanic.ebookrepository.exception.NotFoundException;

import java.util.List;

public interface ICategoryService {

    boolean categoryExists(Long categoryId);

    Category findCategoryById(Long id);

    List<Category> findAllCategories();

    Category saveCategory(Category category);

}
