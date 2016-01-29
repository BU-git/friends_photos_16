package com.bionic.fp.web.security.spring.infrastructure.filter.impl;

import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationStrategy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public abstract class AbstractAuthenticationStrategy implements AuthenticationStrategy {

    /**
     * Sets the authentication in the security context holder
     *
     * @param userDetails the user details, most likely is {@link User}
     * @param request the request
     */
    protected void setAuthentication(final UserDetails userDetails, final HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
