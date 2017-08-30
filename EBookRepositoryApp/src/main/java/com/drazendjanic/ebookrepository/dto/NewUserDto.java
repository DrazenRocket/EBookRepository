package com.drazendjanic.ebookrepository.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NewUserDto {

    @NotNull(message = "error.firstName.NotNull")
    @Size(min = 1, message = "error.firstName.Size")
    private String firstName;

    @NotNull(message = "error.lastName.NotNull")
    @Size(min = 1, message = "error.lastName.Size")
    private String lastName;

    @NotNull(message = "error.username.NotNull")
    @Size(min = 1, message = "error.username.Size")
    private String username;

    @NotNull(message = "error.password.NotNull")
    @Size(min = 1, message = "error.password.Size")
    private String password;

    @NotNull(message = "error.type.NotNull")
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_SUBSCRIBER)$", message = "error.type.Pattern")
    private String type;

    private Long categoryId;

    public NewUserDto() {

    }

    public NewUserDto(String firstName, String lastName, String username, String password, String type, Long categoryId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.type = type;
        this.categoryId = categoryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
