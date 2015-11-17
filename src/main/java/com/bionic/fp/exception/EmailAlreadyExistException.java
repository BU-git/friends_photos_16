package com.bionic.fp.exception;

/**
 * Created by boubdyk on 17.11.2015.
 */

/**
 * Used to tell that requested email already exist in DB.
 */
public class EmailAlreadyExistException extends Exception {

    public EmailAlreadyExistException() {
        super("Email already exist in DB.");
    }
}
