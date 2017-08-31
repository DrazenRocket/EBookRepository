package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.EBook;
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
@RequestMapping("/api/ebooks")
public class EBookController {

    @Autowired
    private IEBookService eBookService;

    @GetMapping("")
    public ResponseEntity<List<EBook>> findAllEBooks() {
        ResponseEntity responseEntity = null;
        List<EBook> eBooks = eBookService.findAllEBooks();

        eBooks.forEach((eBook) -> eBook.getCataloguer().setPassword(null));
        responseEntity = new ResponseEntity<List<EBook>>(eBooks, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/{eBookId}")
    public ResponseEntity<EBook> findOneEBookById(@PathVariable Long eBookId) {
        ResponseEntity<EBook> responseEntity = null;
        EBook eBook = eBookService.findEBookById(eBookId);

        if (eBook != null) {
            eBook.getCataloguer().setPassword(null);
            responseEntity = new ResponseEntity<EBook>(eBook, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<EBook>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

}
