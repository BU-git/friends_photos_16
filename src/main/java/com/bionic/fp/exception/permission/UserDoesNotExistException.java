package com.bionic.fp.exception.permission;

/**
 * Created by Yevhenii on 12/5/2015.
 */
public class UserDoesNotExistException extends EventException {
    public UserDoesNotExistException() {
        super("User does not exist in this event");
    }
}
