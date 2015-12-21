package com.bionic.fp.exception.logic.critical;

import com.bionic.fp.exception.AppException;

/**
 * Signals that received too much entity as a result of the operation
 * which does not meet the business logic of the application
 *
 * @author Sergiy Gabriel
 */
public class NonUniqueResultException extends AppException {

    public NonUniqueResultException(final String message) {
        super(message);
    }
}