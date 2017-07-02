package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends CrudRepository<Category, Long> {

}
