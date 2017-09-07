package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.Language;

import java.util.List;

public interface ILanguageService {

    Language findLanguageById(Long id);

    List<Language> findAllLanguages();

}
