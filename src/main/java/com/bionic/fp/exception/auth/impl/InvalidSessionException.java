package com.bionic.fp.exception.auth.impl;

import com.bionic.fp.exception.auth.AuthenticationException;

/**
 * Signals about invalid session
 *
 * @author Sergiy Gabriel
 */
public class InvalidSessionException extends AuthenticationException {

    public InvalidSessionException() {
        super("This session is invalid");
    }
}
