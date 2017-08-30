package com.drazendjanic.ebookrepository.assembler;

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
}
