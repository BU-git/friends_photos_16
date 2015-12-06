package com.bionic.fp.exception.logic.impl;

import com.bionic.fp.exception.logic.EntityNotFoundException;

/**
 * Signals that has not been found the account, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException(Long accountId) {
        super("account", accountId);
    }

    public AccountNotFoundException(String parameter) {
        super("account", parameter);
    }
}
