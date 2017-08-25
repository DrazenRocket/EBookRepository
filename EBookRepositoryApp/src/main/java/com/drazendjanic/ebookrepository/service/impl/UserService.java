package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.repository.IUserRepository;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User findUserById(Long id) {
        User user = userRepository.findOne(id);

        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        User user =  userRepository.findByUsername(username);

        return user;
    }

    @Override
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

}
