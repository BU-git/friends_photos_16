package com.bionic.fp.exception.app.logic.impl;

import com.bionic.fp.exception.app.logic.EntityNotFoundException;

/**
 * todo: comment
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
