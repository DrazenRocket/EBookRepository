package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.repository.ILanguageRepository;
import com.drazendjanic.ebookrepository.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    private ILanguageRepository languageRepository;

}
