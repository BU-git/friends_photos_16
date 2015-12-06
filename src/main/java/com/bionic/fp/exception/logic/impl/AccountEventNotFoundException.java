package com.bionic.fp.exception.logic.impl;

import com.bionic.fp.exception.logic.EntityNotFoundException;

/**
 * Signals that has not been found the account-event, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class AccountEventNotFoundException extends EntityNotFoundException {

    public AccountEventNotFoundException(final Long accountEventId) {
        super("account-event", accountEventId);
    }

    public AccountEventNotFoundException(final Long accountId, final Long eventId) {
        super(String.format("could not find account-event '%d-%d'.", accountId, eventId));
    }
}
