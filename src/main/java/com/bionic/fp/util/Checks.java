package com.bionic.fp.util;

import com.bionic.fp.exception.app.logic.InvalidParameterException;

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
}