package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.EBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEBookRepository extends CrudRepository<EBook, Long> {

    @Override
    List<EBook> findAll();

    List<EBook> findByCategoryId(Long id);

}
