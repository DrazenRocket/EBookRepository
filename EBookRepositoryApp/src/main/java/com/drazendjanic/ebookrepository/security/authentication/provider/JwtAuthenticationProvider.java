package com.drazendjanic.ebookrepository.security.authentication.provider;

import com.drazendjanic.ebookrepository.entity.User;
import com.drazendjanic.ebookrepository.security.authentication.token.JwtAuthenticationToken;
import com.drazendjanic.ebookrepository.service.IUserService;
import com.drazendjanic.ebookrepository.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IUserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication responseAuthentication = null;
        String jwt = (String)authentication.getPrincipal();
        Long userId = null;
        User user = null;
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (jwt != null && jwt.isEmpty()) {
            throw new BadCredentialsException("Missing json web token (JWT).");
        }

        userId = JwtUtil.unpackJwtSubjectAsLong(jwt);

        if (userId == null) {
            throw new BadCredentialsException("Invalid jwt or some internal service error.");
        }

        user = userService.findUserById(userId);

        if (user == null) {
            throw new BadCredentialsException("Invalid jwt");
        }

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getType()));
        responseAuthentication = new PreAuthenticatedAuthenticationToken(userId, null, grantedAuthorities);

        return responseAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supported = authentication.equals(JwtAuthenticationToken.class);

        return supported;
    }

}
