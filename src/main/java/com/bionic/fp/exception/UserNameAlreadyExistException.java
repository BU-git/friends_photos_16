package com.bionic.fp.exception;

/**
 * Created by boubdyk on 15.11.2015.
 */

/**
 * This exception is used to tell that requested user name already exist in DB.
 */
public class UserNameAlreadyExistException extends Exception {

    public UserNameAlreadyExistException() {
        super("User name already exist.");
    }
}
