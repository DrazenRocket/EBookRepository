package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.Language;
import com.drazendjanic.ebookrepository.repository.ILanguageRepository;
import com.drazendjanic.ebookrepository.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    private ILanguageRepository languageRepository;

    @Override
    @Transactional
    public Language findLanguageById(Long id) {
        Language language = languageRepository.findOne(id);

        return language;
    }

    @Override
    @Transactional
    public List<Language> findAllLanguages() {
        List<Language> languages = languageRepository.findAll();

        return languages;
    }

}
