package com.bionic.fp.security;

import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.auth.impl.InvalidSessionException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.service.AccountService;

import javax.servlet.http.HttpSession;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * Contains session utils for security
 *
 * @author Sergiy Gabriel
 */
public class SessionUtils {

    public static final String USER_ID = "id";

    private final AccountService accountService;

    /**
     * @throws InvalidParameterException if the injection of the account service is not going to happen
     */
    public SessionUtils(final AccountService accountService) throws InvalidParameterException {
        check(accountService != null, "The injection of the account service did not happen");
        this.accountService = accountService;
    }

    /**
     * Sets the user id in the session
     *
     * @param session the session
     * @param account the account
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void setUserId(final HttpSession session, final Account account) throws InvalidParameterException {
        check(account != null, "The account shouldn't be null");
        setUserId(session, account.getId());
    }

    /**
     * Sets the user id in the session
     *
     * @param session the session
     * @param userId the user id
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void setUserId(final HttpSession session, final Long userId) throws InvalidParameterException {
        check(session != null, "The session shouldn't be null");
        check(userId != null, "The account id shouldn't be null");
        session.setAttribute(USER_ID, userId);
    }

    /**
     * Returns the user id for this session
     *
     * @param session the session
     * @return an user id
     * @throws InvalidParameterException if the session is not initialized
     * @throws InvalidSessionException if not found the attribute of the user id
     */
    public static Long getUserId(final HttpSession session) throws InvalidParameterException, InvalidSessionException {
        check(session != null, "The session shouldn't be null");
        return ofNullable((Long) session.getAttribute(USER_ID))
                .orElseThrow(InvalidSessionException::new);
    }

    /**
     * Removes the user id from this session
     * todo: this doesn't work!?!? 403 - Access to the specified resource has been forbidden.
     * @param session the session
     * @throws InvalidParameterException if the session is not initialized
     */
//    public static void removeUserId(final HttpSession session) throws InvalidParameterException {
//        check(session != null, "The session shouldn't be null");
//        session.removeAttribute(USER_ID);
//    }

    /**
     * Returns the user for this session
     *
     * @param session the session
     * @return an user
     * @throws InvalidParameterException if the session is not initialized
     * @throws InvalidSessionException if not found the attribute of the user id or the attribute value is incorrect
     */
    public Account getUser(final HttpSession session) throws InvalidParameterException, InvalidSessionException {
        Long userId = getUserId(session);
        return ofNullable(this.accountService.get(userId)).orElseGet(() -> {
            session.invalidate();
            throw new InvalidSessionException();
        });
    }
}
