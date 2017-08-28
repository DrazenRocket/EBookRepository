package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> findAllCategories();

}
