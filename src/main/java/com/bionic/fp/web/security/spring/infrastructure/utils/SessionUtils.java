package com.bionic.fp.web.security.spring.infrastructure.utils;

import com.bionic.fp.exception.auth.impl.InvalidSessionException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.web.security.spring.infrastructure.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.bionic.fp.util.Checks.checkNotNull;
import static java.util.Optional.ofNullable;

/**
 * Contains session utils for security
 *
 * @author Sergiy Gabriel
 */
public class SessionUtils {

    private static final String USER = "user";

    /**
     * Sets an user in the session
     *
     * @param user the user
     * @param session the session
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void setUser(final User user, final HttpSession session) throws InvalidParameterException {
        checkNotNull(session, "session");
        checkNotNull(user, "user");
        session.setAttribute(USER, user);
    }

    /**
     * Sets an user in the session
     *
     * @param user the user
     * @param request the request
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void setUser(final User user, final HttpServletRequest request) throws InvalidParameterException {
        checkNotNull(request, "request");
        HttpSession session = request.getSession(true);
        setUser(user, session);
    }

//    /**
//     * Returns an user for this session
//     *
//     * @param session the session
//     * @return an user
//     * @throws InvalidParameterException if the session is not initialized
//     * @throws InvalidSessionException if not found the attribute of the user id
//     */
//    public static User getUser(final HttpSession session) throws InvalidParameterException, InvalidSessionException {
//        checkNotNull(session, "session");
//        return ofNullable((User) session.getAttribute(USER)).orElseThrow(InvalidSessionException::new);
//    }
//
//    /**
//     * Returns an user for this session
//     *
//     * @param request the request
//     * @return an user
//     * @throws InvalidParameterException if the session is not initialized
//     * @throws InvalidSessionException if not found the attribute of the user
//     */
//    public static User getUser(final HttpServletRequest request) throws InvalidParameterException, InvalidSessionException {
//        checkNotNull(request, "request");
//        HttpSession session = ofNullable(request.getSession(false)).orElseThrow(InvalidSessionException::new);
//        return getUser(session);
//    }

    /**
     * Returns an user for this session, or null if the user is not set in the session yet
     *
     * @param session the session
     * @return an user
     * @throws InvalidParameterException if the session is not initialized
     */
    public static User getUser(final HttpSession session) throws InvalidParameterException {
        checkNotNull(session, "session");
        return (User) session.getAttribute(USER);
    }

    /**
     * Returns an user for this session, or null if the user is not set in the session yet
     *
     * @param request the request
     * @return an user
     * @throws InvalidParameterException if the session is not initialized
     */
    public static User getUser(final HttpServletRequest request) throws InvalidParameterException {
        checkNotNull(request, "request");
        HttpSession session = request.getSession(false);
        return session != null ? getUser(session) : null;
    }

    /**
     * Invalidates this session
     *
     * @param session the session
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void logout(final HttpSession session) throws InvalidParameterException {
        checkNotNull(session, "session");
        session.invalidate();
    }

    /**
     * Invalidates the session
     *
     * @param request the request
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void logout(final HttpServletRequest request) throws InvalidParameterException {
        checkNotNull(request, "request");
        HttpSession session = request.getSession(false);
        if (session != null) {
            logout(session);
        }
    }
}
