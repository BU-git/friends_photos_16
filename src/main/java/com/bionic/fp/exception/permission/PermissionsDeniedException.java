package com.bionic.fp.exception.permission;

import com.bionic.fp.exception.AppException;

/**
 * Created by Yevhenii on 12/5/2015.
 */
public class PermissionsDeniedException extends AppException {
    public PermissionsDeniedException(){
        super("You are not allowed to perform this operation");
    }
}
