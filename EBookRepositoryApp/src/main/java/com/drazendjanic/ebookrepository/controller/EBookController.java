package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.EBook;
import com.drazendjanic.ebookrepository.service.IEBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
public class EBookController {

    @Autowired
    private IEBookService eBookService;

    @GetMapping("")
    public ResponseEntity<List<EBook>> findAllEBooks() {
        ResponseEntity responseEntity = null;
        List<EBook> eBooks = eBookService.findAllEBooks();

        responseEntity = new ResponseEntity<List<EBook>>(eBooks, HttpStatus.OK);

        return responseEntity;
    }
}
