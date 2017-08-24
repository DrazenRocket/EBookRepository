package com.drazendjanic.ebookrepository.dto;

public class JwtDto {

    private Long userId;

    private String token;

    public JwtDto() {

    }

    public JwtDto(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
