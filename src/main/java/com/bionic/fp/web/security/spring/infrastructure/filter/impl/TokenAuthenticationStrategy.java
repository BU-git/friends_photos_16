package com.bionic.fp.web.security.spring.infrastructure.filter.impl;

import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public abstract class TokenAuthenticationStrategy extends AbstractAuthenticationStrategy {

    @Autowired protected TokenUtils tokenUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authToken = this.getToken(httpRequest);

        if(SecurityContextHolder.getContext().getAuthentication() == null && this.tokenUtils.validateToken(authToken)) {
            User user = this.tokenUtils.extractUser(authToken);
            if(user != null && user.validate()) {
                setAuthentication(user, httpRequest);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The user is invalid");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Retrieves the token from the request
     *
     * @param request the request
     * @return the token or null if the token is not found in the request
     */
    protected abstract String getToken(HttpServletRequest request);
}
