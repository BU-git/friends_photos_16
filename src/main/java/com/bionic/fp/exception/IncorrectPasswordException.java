package com.bionic.fp.exception;

/**
 * Created by boubdyk on 15.11.2015.
 */

/**
 * This exception is used to tell that password for such user is incorrect.
 */
public class IncorrectPasswordException extends Exception {

    public IncorrectPasswordException() {
        super("Incorrect password for requested user name.");
    }
}
