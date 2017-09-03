package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILanguageRepository extends CrudRepository<Language, Long> {

    @Override
    List<Language> findAll();

}
