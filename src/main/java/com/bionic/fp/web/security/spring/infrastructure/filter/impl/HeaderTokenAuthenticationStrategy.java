package com.bionic.fp.web.security.spring.infrastructure.filter.impl;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the authentication strategy implementation which only retrieves the certain token from the request header
 *
 * @author Sergiy Gabriel
 */
public class HeaderTokenAuthenticationStrategy extends TokenAuthenticationStrategy {

    @Value("${token.header}") private String tokenHeader;

    @Override
    protected String getToken(HttpServletRequest request) {
        return request.getHeader(this.tokenHeader);
    }

}