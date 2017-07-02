package com.drazendjanic.ebookrepository.repository;

import com.drazendjanic.ebookrepository.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {

}
