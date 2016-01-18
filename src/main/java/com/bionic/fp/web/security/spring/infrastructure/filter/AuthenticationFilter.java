package com.bionic.fp.web.security.spring.infrastructure.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Authentication filter
 *
 * @author Sergiy Gabriel
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationStrategy authenticationStrategy;

    @Autowired
    public AuthenticationFilter(AuthenticationStrategy authenticationStrategy) {
        this.authenticationStrategy = authenticationStrategy;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.authenticationStrategy.doFilter(request, response, chain);
    }
}