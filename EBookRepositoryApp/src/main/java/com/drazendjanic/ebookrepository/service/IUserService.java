package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.User;

import java.util.List;

public interface IUserService {

    User findUserById(Long id);

    User findUserByUsername(String username);

    List<User> findUsers();

}
