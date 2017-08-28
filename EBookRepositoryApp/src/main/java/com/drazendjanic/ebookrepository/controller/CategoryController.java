package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.Category;
import com.drazendjanic.ebookrepository.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Category>> findAllCategories() {
        ResponseEntity<List<Category>> responseEntity = null;
        List<Category> categories = categoryService.findAllCategories();

        responseEntity = new ResponseEntity<List<Category>>(categories, HttpStatus.OK);

        return responseEntity;
    }

}
