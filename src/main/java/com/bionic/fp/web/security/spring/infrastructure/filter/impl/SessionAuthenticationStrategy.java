package com.bionic.fp.web.security.spring.infrastructure.filter.impl;

import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.utils.SessionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class SessionAuthenticationStrategy extends AbstractAuthenticationStrategy {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        User user = SessionUtils.getUser(httpRequest);
        if(SecurityContextHolder.getContext().getAuthentication() == null && user != null && user.validate()) {
            setAuthentication(user, httpRequest);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void saveAuthentication(User user, HttpServletRequest request, HttpServletResponse response) {
        SessionUtils.setUser(user, request);
    }

    @Override
    public void removeAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SessionUtils.logout(request);
    }
}
