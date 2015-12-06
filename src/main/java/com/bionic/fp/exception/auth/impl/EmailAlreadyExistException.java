package com.bionic.fp.exception.auth.impl;

import com.bionic.fp.exception.auth.AuthenticationException;

/**
 * Used to tell that requested email already exist in DB.
 *
 * Created by boubdyk on 17.11.2015.
 */
public class EmailAlreadyExistException extends AuthenticationException {

    public EmailAlreadyExistException() {
        super("Email already exist in DB.");
    }
}
