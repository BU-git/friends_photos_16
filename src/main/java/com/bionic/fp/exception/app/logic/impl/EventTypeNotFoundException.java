package com.bionic.fp.exception.app.logic.impl;

import com.bionic.fp.exception.app.logic.EntityNotFoundException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class EventTypeNotFoundException extends EntityNotFoundException {

    public EventTypeNotFoundException(Integer eventTypeId) {
        super("event type", eventTypeId.longValue());
    }
}
