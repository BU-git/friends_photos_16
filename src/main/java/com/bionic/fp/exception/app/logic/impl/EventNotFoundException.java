package com.bionic.fp.exception.app.logic.impl;

import com.bionic.fp.exception.app.logic.EntityNotFoundException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class EventNotFoundException extends EntityNotFoundException {

    public EventNotFoundException(Long eventId) {
        super("event", eventId);
    }
}
