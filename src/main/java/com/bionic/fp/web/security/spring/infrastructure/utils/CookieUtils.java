package com.bionic.fp.web.security.spring.infrastructure.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Contains cookie utils
 *
 * @author Sergiy Gabriel
 */
public class CookieUtils {

    private CookieUtils() {}

    public static String getCookie(final String cookieName, final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookieName == null || cookies == null || cookies.length == 0) {
            return null;
        }

        for(Cookie cookie : cookies) {
            if(cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public static void setCookie(final String cookieName, final String value, final int maxAge,
                                 final HttpServletRequest request, final HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(getCookiePath(request));
        cookie.setSecure(request.isSecure());

        response.addCookie(cookie);
    }

    public static void cancelCookie(final String cookieName,
                                    final HttpServletRequest request, final HttpServletResponse response) {
        setCookie(cookieName, null, 0, request, response);
    }

    private static String getCookiePath(final HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
}
