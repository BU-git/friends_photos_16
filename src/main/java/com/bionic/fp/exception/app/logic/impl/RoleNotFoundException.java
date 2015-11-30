package com.bionic.fp.exception.app.logic.impl;

import com.bionic.fp.exception.app.logic.EntityNotFoundException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException(Integer roleId) {
        super("role", roleId.longValue());
    }
}
