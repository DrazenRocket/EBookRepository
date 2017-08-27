package com.drazendjanic.ebookrepository.service.impl;

import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.exception.InvalidPasswordException;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.repository.IUserRepository;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional
    public User findUserById(Long id) {
        User user = userRepository.findOne(id);

        return user;
    }

    @Override
    @Transactional
    public User findUserByUsername(String username) {
        User user =  userRepository.findByUsername(username);

        return user;
    }

    @Override
    @Transactional
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword)
            throws NotFoundException, InvalidPasswordException {
        User user = findUserById(userId);

        if (user != null) {
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
            }
            else {
                throw new InvalidPasswordException("Old password for user with id " + userId + " is invalid.");
            }
        }
        else {
            throw new NotFoundException("User with id " + userId + " is not found.");
        }
    }

}
