package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.Language;
import com.drazendjanic.ebookrepository.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    @Autowired
    private ILanguageService languageService;

    @GetMapping("")
    public ResponseEntity<List<Language>> findAllLanguages() {
        ResponseEntity<List<Language>> responseEntity = null;
        List<Language> languages = languageService.findAllLanguages();

        responseEntity = new ResponseEntity<List<Language>>(languages, HttpStatus.OK);

        return responseEntity;
    }

}
