package com.bionic.fp.exception.app.logic.impl;

import com.bionic.fp.exception.app.logic.EntityNotFoundException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException(Long accountId) {
        super("account", accountId);
    }
}
