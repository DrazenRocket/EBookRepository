package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.User;

public interface IUserService {

    User findUserById(Long id);

    User findUserByUsername(String username);

}
