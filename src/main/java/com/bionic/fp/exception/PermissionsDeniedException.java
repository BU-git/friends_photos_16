package com.bionic.fp.exception;

/**
 * Created by Yevhenii on 12/5/2015.
 */
public class PermissionsDeniedException extends RuntimeException {
    public PermissionsDeniedException(){
        super("You are not allowed to perform this operation");
    }
}
