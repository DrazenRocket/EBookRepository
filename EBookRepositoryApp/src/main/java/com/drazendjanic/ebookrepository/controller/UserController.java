package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.assembler.UserAssembler;
import com.drazendjanic.ebookrepository.dto.ChangePasswordDto;
import com.drazendjanic.ebookrepository.dto.EditedUserDto;
import com.drazendjanic.ebookrepository.dto.NewUserDto;
import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.exception.InvalidPasswordException;
import com.drazendjanic.ebookrepository.exception.NotFoundException;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> findAllUsers() {
        ResponseEntity<List<User>> responseEntity = null;
        List<User> users = userService.findAllUsers();

        users.forEach(user -> user.setPassword(null));
        responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findOneUserById(@PathVariable Long userId) {
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

    @PostMapping("")
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> addUser(@Validated @RequestBody NewUserDto newUserDto) {
        ResponseEntity<User> responseEntity = null;
        User savedUser = null;
        User newUser = UserAssembler.toUser(newUserDto);
        boolean usedUsername = userService.usedUsername(newUser.getUsername());

        if (usedUsername || (newUser.getType().equals("ROLE_ADMIN") && newUser.getCategory() != null)) {
            responseEntity = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        else {
            savedUser = userService.saveUser(newUser);
            responseEntity = new ResponseEntity<User>(savedUser, HttpStatus.OK);
        }

        return responseEntity;
    }

    @PutMapping("/{userId}/info")
    @PreAuthorize("isAuthenticated() && (#authenticatedUserId == #userId || hasRole('ROLE_ADMIN'))")
    public ResponseEntity<Void> editUser(@AuthenticationPrincipal Long authenticatedUserId,
                                         @PathVariable Long userId,
                                         @RequestBody EditedUserDto editedUserDto) {
        ResponseEntity<Void> responseEntity = null;
        User editedUser = UserAssembler.toUser(editedUserDto);
        User user = userService.findUserById(userId);
        boolean usedUsername = userService.usedUsername(editedUser.getUsername());
        boolean validUsername = false;

        if (user != null) {
            validUsername = !usedUsername || user.getUsername().equals(editedUser.getUsername());

            if (validUsername && !(editedUser.getType().equals("ROLE_ADMIN") && editedUser.getCategory() != null)) {
                user.setFirstName(editedUser.getFirstName());
                user.setLastName(editedUser.getLastName());
                user.setUsername(editedUser.getUsername());
                user.setType(editedUser.getType());
                user.setCategory(editedUser.getCategory());
                userService.saveUser(user);
                responseEntity = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
            else {
                responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("isAuthenticated() && (#authenticatedUserId == #userId || hasRole('ROLE_ADMIN'))")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal Long authenticatedUserId,
                                               @PathVariable Long userId,
                                               @Validated @RequestBody ChangePasswordDto changePasswordDto) {
        ResponseEntity<Void> responseEntity = null;
        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            responseEntity = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        catch (NotFoundException e) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        catch (InvalidPasswordException e1) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e2) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

}
