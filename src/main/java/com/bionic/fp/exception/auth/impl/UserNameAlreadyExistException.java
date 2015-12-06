package com.bionic.fp.exception.auth.impl;

import com.bionic.fp.exception.auth.AuthenticationException;

/**
 * This exception is used to tell that requested user name already exist in DB.
 *
 * Created by boubdyk on 15.11.2015.
 */
public class UserNameAlreadyExistException extends AuthenticationException {

    public UserNameAlreadyExistException() {
        super("User name already exist.");
    }
}
