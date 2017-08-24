package com.drazendjanic.ebookrepository.controller;

import com.drazendjanic.ebookrepository.dto.JwtDto;
import com.drazendjanic.ebookrepository.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentications")
public class AuthenticationController {

    @GetMapping("/jwt")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JwtDto> getJwt(@AuthenticationPrincipal Long authenticatedUserId) {
        ResponseEntity<JwtDto> responseEntity = null;
        String jwt = JwtUtil.generateJwt(authenticatedUserId);
        JwtDto jwtDto = new JwtDto(authenticatedUserId, jwt);

        if (jwt != null) {
            responseEntity = new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<JwtDto>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

}
