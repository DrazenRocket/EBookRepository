package com.drazendjanic.ebookrepository.service;

import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.exception.InvalidPasswordException;
import com.drazendjanic.ebookrepository.exception.NotFoundException;

import java.util.List;

public interface IUserService {

    User findUserById(Long id);

    User findUserByUsername(String username);

    List<User> findUsers();

    void changePassword(Long userId, String oldPassword, String newPassword) throws NotFoundException, InvalidPasswordException;

}
