package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.Category;
import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.service.ICategoryService;
import com.drazendjanic.ebookrepository.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IEBookService eBookService;

    @GetMapping("")
    public ResponseEntity<List<Category>> findAllCategories() {
        ResponseEntity<List<Category>> responseEntity = null;
        List<Category> categories = categoryService.findAllCategories();

        responseEntity = new ResponseEntity<List<Category>>(categories, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> findOneCategoryById(@PathVariable Long categoryId) {
        ResponseEntity<Category> responseEntity = null;
        Category category = categoryService.findCategoryById(categoryId);

        if (category != null) {
            responseEntity = new ResponseEntity<Category>(category, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<Category>(category, HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @GetMapping("/{categoryId}/ebooks")
    public ResponseEntity<List<EBook>> findAllBooksByCategoryId(@PathVariable Long categoryId) {
        ResponseEntity<List<EBook>> responseEntity = null;
        List<EBook> eBooks = eBookService.findAllEBooksByCategoryId(categoryId);

        eBooks.stream().forEach((eBook) -> eBook.getCataloguer().setPassword(null));
        responseEntity = new ResponseEntity<List<EBook>>(eBooks, HttpStatus.OK);

        return responseEntity;
    }

}
