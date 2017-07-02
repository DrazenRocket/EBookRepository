package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILanguageRepository extends CrudRepository<Language, Long> {

}
