package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends CrudRepository<Category, Long> {

    @Override
    List<Category> findAll();

}
