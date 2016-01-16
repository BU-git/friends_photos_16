package com.bionic.fp.dao;

import com.bionic.fp.domain.EventType;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;

/**
 * Represents data access object of the event type
 *
 * @author Sergiy Gabriel
 */
public interface EventTypeDAO extends GenericDAO<EventType, Long> {

    /**
     * Returns the private type of the event from database
     *
     * @return the private type of the event
     * @throws EventTypeNotFoundException if the event type doesn't exist
     */
    EventType getPrivate() throws EventTypeNotFoundException;
}
