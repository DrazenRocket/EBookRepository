package com.drazendjanic.ebookrepository.dto;

public class JwtDto {

    private Long userId;

    private String jwt;

    public JwtDto() {

    }

    public JwtDto(Long userId, String jwt) {
        this.userId = userId;
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
