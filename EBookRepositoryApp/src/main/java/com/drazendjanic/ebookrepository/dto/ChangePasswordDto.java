package com.drazendjanic.ebookrepository.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordDto {

    @NotNull(message = "error.oldPassword.NotNull")
    @Size(min = 1, message = "error.oldPassword.Size")
    private String oldPassword;

    @NotNull(message = "error.newPassword.NotNull")
    @Size(min = 1, message = "error.newPassword.Size")
    private String newPassword;

    public ChangePasswordDto() {

    }

    public ChangePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
