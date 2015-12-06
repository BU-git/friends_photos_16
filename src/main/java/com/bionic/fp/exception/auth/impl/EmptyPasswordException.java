package com.bionic.fp.exception.auth.impl;

import com.bionic.fp.exception.auth.AuthenticationException;

/**
 * This exception
 *
 * Created by boubdyk on 15.11.2015.
 */
public class EmptyPasswordException extends AuthenticationException {

    public EmptyPasswordException() {
        super("Password is empty.");
    }
}
