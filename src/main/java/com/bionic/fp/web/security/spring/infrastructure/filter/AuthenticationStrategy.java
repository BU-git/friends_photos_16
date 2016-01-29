package com.bionic.fp.web.security.spring.infrastructure.filter;

import com.bionic.fp.web.security.spring.infrastructure.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class represents authentication strategy
 *
 * @author Sergiy Gabriel
 */
public interface AuthenticationStrategy {

    /**
     * Will be invoked by the incoming request, which allows to set the authentication for further use
     */
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;

    /**
     * Should be called when a successful authentication, gives the ability to save
     * the necessary attributes for use in the following requests (if provided)
     *
     * @param user the user details
     */
    default void saveAuthentication(User user, HttpServletRequest request, HttpServletResponse response) {}

    /**
     * Should be called when user logout
     */
    default void removeAuthentication(HttpServletRequest request, HttpServletResponse response) {}
}
