package com.bionic.fp.exception.logic.impl;

import com.bionic.fp.exception.logic.EntityNotFoundException;

/**
 * Signals that has not been found the event type, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class EventTypeNotFoundException extends EntityNotFoundException {

    public EventTypeNotFoundException(Integer eventTypeId) {
        super("event type", eventTypeId.longValue());
    }
}
