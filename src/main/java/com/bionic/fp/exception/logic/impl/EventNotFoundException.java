package com.bionic.fp.exception.logic.impl;

import com.bionic.fp.exception.logic.EntityNotFoundException;

/**
 * Signals that has not been found the event, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class EventNotFoundException extends EntityNotFoundException {

    public EventNotFoundException(Long eventId) {
        super("event", eventId);
    }
}
