package com.bionic.fp.web.security.spring.infrastructure.filter.impl;

import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the authentication strategy implementation which saves and retrieves the certain token from the cookie
 *
 * @author Sergiy Gabriel
 */
public class CookieTokenAuthenticationStrategy extends TokenAuthenticationStrategy {

    @Value("${token.cookie}")       private String cookieToken;
    @Value("${token.expiration}")   private int expiration;

    @Override
    public void saveAuthentication(User user, HttpServletRequest request, HttpServletResponse response) {
        String token = this.tokenUtils.generateToken(user);
        CookieUtils.setCookie(this.cookieToken, token, this.expiration, request, response);
    }

    @Override
    protected String getToken(HttpServletRequest request) {
        return CookieUtils.getCookie(this.cookieToken, request);
    }

    @Override
    public void removeAuthentication(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.cancelCookie(this.cookieToken, request, response);
    }
}