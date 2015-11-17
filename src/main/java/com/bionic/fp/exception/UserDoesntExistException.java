package com.bionic.fp.exception;

/**
 * Created by boubdyk on 15.11.2015.
 */

/**
 * This exception is used to tell that such user doesn't exist in DB.
 */
public class UserDoesntExistException extends Exception {

    public UserDoesntExistException() {
        super("User doesn't exist in DB.");
    }
}
