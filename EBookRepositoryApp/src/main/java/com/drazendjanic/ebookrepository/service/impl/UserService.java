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
    public boolean userExists(Long id) {
        boolean exists = userRepository.exists(id);

        return exists;
    }

    @Override
    @Transactional
    public boolean usedUsername(String username) {
        boolean used = false;

        if (findUserByUsername(username) != null) {
            used = true;
        }

        return used;
    }

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
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);

        return savedUser;
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
