package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.EBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEBookRepository extends CrudRepository<EBook, Long> {

}
