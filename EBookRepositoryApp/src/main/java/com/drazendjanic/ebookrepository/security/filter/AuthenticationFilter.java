package com.drazendjanic.ebookrepository.security.filter;

import com.drazendjanic.ebookrepository.security.authentication.token.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String username = httpRequest.getHeader("X-Auth-Username");
        String password = httpRequest.getHeader("X-Auth-Password");
        String jwt = httpRequest.getHeader("X-Auth-Jwt");

        try {
            if (jwt == null) {
                if (username != null && password != null) {
                    processUsernamePasswordAuthentication(username, password);
                }
            }
            else {
                    processJwtAuthentication(jwt);
            }

            chain.doFilter(request, response);
        }
        catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, internalAuthenticationServiceException.getMessage());
        }
    }

    private void processUsernamePasswordAuthentication(String username, String password) {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);

        processAuthenticationRequest(authenticationRequest);
    }

    private void processJwtAuthentication(String jwt) {
        Authentication authenticationRequest = new JwtAuthenticationToken(jwt);

        processAuthenticationRequest(authenticationRequest);
    }

    private void processAuthenticationRequest(Authentication authenticationRequest) {
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse == null || !authenticationResponse.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate user for provided credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
    }

}
