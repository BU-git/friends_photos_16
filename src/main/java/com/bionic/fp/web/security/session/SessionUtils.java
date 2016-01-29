package com.bionic.fp.web.security.session;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.auth.impl.InvalidSessionException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import com.bionic.fp.exception.permission.PermissionsDeniedException;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.RoleService;

import javax.servlet.http.HttpSession;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Contains session utils for security
 *
 * @author Sergiy Gabriel
 */
public class SessionUtils {

    private static final String USER_ID = "id";

    private final AccountService accountService;

    private final RoleService roleService;

    /**
     * @throws InvalidParameterException if the injection of the account service is not going to happen
     */
    public SessionUtils(final AccountService accountService, final RoleService roleService) throws InvalidParameterException {
        check(accountService != null, "The injection of the account service did not happen");
        check(roleService != null, "The injection of the role service did not happen");
        this.accountService = accountService;
        this.roleService = roleService;
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
     * Invalidates this session
     *
     * @param session the session
     * @throws InvalidParameterException if the session is not initialized
     */
    public static void logout(final HttpSession session) throws InvalidParameterException {
        check(session != null, "The session shouldn't be null");
        session.invalidate();
    }

    /**
     * Returns the user for this session
     *
     * @param session the session
     * @param accountService the account service
     * @return an user
     * @throws InvalidParameterException if the session or the account service are not initialized
     * @throws InvalidSessionException if not found the attribute of the user id or the attribute value is incorrect
     */
    public static Account getUser(final HttpSession session, final AccountService accountService) throws InvalidParameterException, InvalidSessionException {
        Long userId = getUserId(session);
        check(accountService != null, "The account service shouldn't be null");
        return ofNullable(accountService.get(userId)).orElseGet(() -> {
            logout(session);
            throw new InvalidSessionException();
        });
    }

    /**
     * Checks the permissions for operations within the event
     *
     * @param session the session
     * @param roleService the role service
     * @param eventId the event id
     * @param predicates the predicates
     * @throws InvalidParameterException if the arguments are not initialized
     * @throws PermissionsDeniedException if no permission to perform operations
     */
    @SafeVarargs
    public static void checkPermission(final HttpSession session, final RoleService roleService,
                                       final Long eventId, final Predicate<Role>... predicates) throws InvalidParameterException, PermissionsDeniedException {
        check(roleService != null, "The role service shouldn't be null");
        if(isNotEmpty(predicates)) {
            Long userId = getUserId(session);
            try {
                Role role = roleService.getRole(userId, eventId);
                if(Stream.of(predicates).anyMatch(p -> p.negate().test(role))) {
                    throw new PermissionsDeniedException();
                }
            } catch (AccountEventNotFoundException e) {
                throw new PermissionsDeniedException();
            }
        }
    }

    /**
     * Returns the user for this session
     *
     * @param session the session
     * @return an user
     * @throws InvalidParameterException if the session is not initialized
     * @throws InvalidSessionException if not found the attribute of the user id or the attribute value is incorrect
     */
    public final Account getUser(final HttpSession session) throws InvalidParameterException, InvalidSessionException {
        return getUser(session, this.accountService);
    }

    /**
     * Checks the permissions for operations within the event
     *
     * @param session the session
     * @param eventId the event id
     * @param predicates the predicates
     * @throws InvalidParameterException if the arguments are not initialized
     * @throws PermissionsDeniedException if no permission to perform operations
     */
    @SafeVarargs
    public final void checkPermission(final HttpSession session, final Long eventId, final Predicate<Role>... predicates) throws InvalidParameterException, PermissionsDeniedException {
        checkPermission(session, this.roleService, eventId, predicates);
    }
}
