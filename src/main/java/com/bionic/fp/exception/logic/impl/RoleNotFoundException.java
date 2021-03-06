package com.bionic.fp.exception.logic.impl;

import com.bionic.fp.exception.logic.EntityNotFoundException;

/**
 * Signals that has not been found the role, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException(Long roleId) {
        super("role", roleId);
    }
}
