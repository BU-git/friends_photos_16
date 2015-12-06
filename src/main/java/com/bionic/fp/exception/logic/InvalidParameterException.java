package com.bionic.fp.exception.logic;

import com.bionic.fp.exception.AppException;

/**
 * Signals that incorrect parameter was passed to method/constructor
 *
 * @author Sergiy Gabriel
 */
public class InvalidParameterException extends AppException {

    public InvalidParameterException(final String message) {
        super(message);
    }
}