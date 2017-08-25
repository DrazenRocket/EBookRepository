package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        ResponseEntity<List<User>> responseEntity = null;
        List<User> users = userService.findUsers();

        users.forEach(user -> user.setPassword(null));
        responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        ResponseEntity<User> responseEntity = null;
        User user = userService.findUserById(userId);

        if (user != null) {
            user.setPassword(null);
            responseEntity = new ResponseEntity<User>(user, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

}