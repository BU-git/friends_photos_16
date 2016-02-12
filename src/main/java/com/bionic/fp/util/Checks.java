package com.bionic.fp.util;

import com.bionic.fp.domain.*;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import com.bionic.fp.exception.logic.InvalidParameterException;

/**
 * Contains common check routines
 *
 * @author Sergiy Gabriel
 */
public class Checks {

    private Checks() {
    }

    /**
     * Verifies that expected value passed and throws the exception otherwise
     *
     * @param expected the expected value
     * @param message the detail message for the thrown exception
     * @throws InvalidParameterException if the expected value is false
     */
    public static void check(final boolean expected, final String message) throws InvalidParameterException {
        if(!expected) {
            throw new InvalidParameterException(message);
        }
    }

    /**
     * Verifies that expected value passed and throws the specified exception otherwise
     *
     * @param expected the expected value
     * @param e the runtime exception
     * @throws RuntimeException if the expected value is false
     */
    public static void check(final boolean expected, final RuntimeException e) throws RuntimeException {
        if(!expected) {
            throw e;
        }
    }

    /**
     * Verifies that target object is not null and throws the exception otherwise
     *
     * @param target the target object
     * @param targetName the name of the target object
     * @throws InvalidParameterException if the target object is null
     */
    public static void checkNotNull(final Object target, final String targetName) throws InvalidParameterException {
        check(target != null, String.format("The %s shouldn't be null", targetName));
    }

    public static void checkAccountEvent(final AccountEvent accountEvent) throws IncorrectPasswordException {
        checkNotNull(accountEvent, "account event");
    }

    public static void checkAccountEvent(final Long accountEventId) throws IncorrectPasswordException {
        checkNotNull(accountEventId, "account event id");
    }

    public static void checkEvent(final Event event) throws IncorrectPasswordException {
        checkNotNull(event, "event");
    }

    public static void checkEvent(final Long eventId) throws IncorrectPasswordException {
        checkNotNull(eventId, "event id");
    }

    public static void checkAccount(final Account account) throws IncorrectPasswordException {
        checkNotNull(account, "account");
    }

    public static void checkAccount(final Long accountId) throws IncorrectPasswordException {
        checkNotNull(accountId, "account id");
    }

    public static void checkRole(final Role role) throws IncorrectPasswordException {
        checkNotNull(role, "role");
    }

    public static void checkRole(final Long roleId) throws IncorrectPasswordException {
        checkNotNull(roleId, "role id");
    }

    public static void checkComment(final Comment comment) throws IncorrectPasswordException {
        checkNotNull(comment, "comment");
    }

    public static void checkComment(final Long commentId) throws IncorrectPasswordException {
        checkNotNull(commentId, "comment id");
    }

    public static void checkPhoto(final Photo photo) throws IncorrectPasswordException {
        checkNotNull(photo, "photo");
    }

    public static void checkPhoto(final Long photoId) throws IncorrectPasswordException {
        checkNotNull(photoId, "photo id");
    }
}
