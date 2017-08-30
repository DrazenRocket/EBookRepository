package com.drazendjanic.ebookrepository.assembler;

import com.drazendjanic.ebookrepository.dto.EditedUserDto;
import com.drazendjanic.ebookrepository.dto.NewUserDto;
import com.drazendjanic.ebookrepository.entity.Category;
import com.drazendjanic.ebookrepository.entity.User;

public class UserAssembler {

    public static User toUser(NewUserDto newUserDto) {
        User user = new User();

        user.setFirstName(newUserDto.getFirstName());
        user.setLastName(newUserDto.getLastName());
        user.setUsername(newUserDto.getUsername());
        user.setPassword(newUserDto.getPassword());
        user.setType(newUserDto.getType());

        if (newUserDto.getCategoryId() != null) {
            Category category = new Category();

            category.setId(newUserDto.getCategoryId());
            user.setCategory(category);
        }

        return user;
    }

    public static User toUser(EditedUserDto editedUserDto) {
        User user = new User();

        user.setFirstName(editedUserDto.getFirstName());
        user.setLastName(editedUserDto.getLastName());
        user.setUsername(editedUserDto.getUsername());
        user.setType(editedUserDto.getType());

        if (editedUserDto.getCategoryId() != null) {
            Category category = new Category();

            category.setId(editedUserDto.getCategoryId());
            user.setCategory(category);
        }

        return user;
    }
}
