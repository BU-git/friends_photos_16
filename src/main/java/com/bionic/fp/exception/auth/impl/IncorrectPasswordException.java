package com.bionic.fp.exception.auth.impl;

import com.bionic.fp.exception.auth.AuthenticationException;

/**
 * This exception is used to tell that password for such user is incorrect.
 *
 * Created by boubdyk on 15.11.2015.
 */
public class IncorrectPasswordException extends AuthenticationException {

    public IncorrectPasswordException() {
        super("Wrong email or password");
    }

    public IncorrectPasswordException(final String msg) {
        super("Incorrect password. " + msg);
    }
}
