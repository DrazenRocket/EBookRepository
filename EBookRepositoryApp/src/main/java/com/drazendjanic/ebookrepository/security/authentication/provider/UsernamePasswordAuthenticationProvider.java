package com.drazendjanic.ebookrepository.security.authentication.provider;

import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IUserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication responseAuthentication = null;
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();
        User user = null;
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (username == null || password == null) {
            throw new BadCredentialsException("Missing info for authentication (username and/or password).");
        }

        user = userService.findUserByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid info for authentication (username and/or password).");
        }

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getType()));
        responseAuthentication = new PreAuthenticatedAuthenticationToken(user.getId(), null, grantedAuthorities);

        return responseAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supported = authentication.equals(UsernamePasswordAuthenticationToken.class);

        return supported;
    }

}
