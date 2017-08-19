package com.drazendjanic.ebookrepository.security;

import com.drazendjanic.ebookrepository.security.authentication.provider.JwtAuthenticationProvider;
import com.drazendjanic.ebookrepository.security.authentication.provider.UsernamePasswordAuthenticationProvider;
import com.drazendjanic.ebookrepository.security.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());

        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
        httpSecurity.addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(usernamePasswordAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider usernamePasswordAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = new UsernamePasswordAuthenticationProvider();

        return authenticationProvider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = new JwtAuthenticationProvider();

        return authenticationProvider;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
