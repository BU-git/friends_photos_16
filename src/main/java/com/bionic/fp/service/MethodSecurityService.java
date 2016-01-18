package com.bionic.fp.service;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.InvalidParameterException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;

import java.util.function.Predicate;

/**
 * Contains methods to check access methods and obtaining information about the user
 *
 * @author Sergiy Gabriel
 */
public interface MethodSecurityService {

    /**
     * Checks the permissions for operations within the event
     *
     * @param eventId the event id
     * @param predicates the predicates
     * @throws InvalidParameterException if the arguments are not initialized
     * @throws AccessDeniedException if no permission to perform operations
     */
    void checkPermission(final Long eventId, final Predicate<Role>... predicates) throws InvalidParameterException, AccessDeniedException;

    /**
     * Returns an user id
     *
     * @return an user id
     */
    Long getUserId();

    /**
     * Returns an user email
     *
     * @return an user email
     */
    String getUserEmail();

    /**
     * Returns an user account
     *
     * @return an user account
     * @throws DisabledException if the user was not authenticated or its authentication has become incorrect
     */
    Account getUser() throws DisabledException;
}
