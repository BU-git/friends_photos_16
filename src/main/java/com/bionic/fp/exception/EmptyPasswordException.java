package com.bionic.fp.exception;

/**
 * Created by boubdyk on 15.11.2015.
 */

/**
 * This exception
 */
public class EmptyPasswordException extends Exception {

    public EmptyPasswordException() {
        super("Password is empty.");
    }
}
