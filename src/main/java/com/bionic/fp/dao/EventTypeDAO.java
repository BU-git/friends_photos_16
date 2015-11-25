package com.bionic.fp.dao;

import com.bionic.fp.domain.EventType;

/**
 * Represents data access object of the event type
 *
 * @author Sergiy Gabriel
 */
public interface EventTypeDAO extends GenericDAO<EventType, Integer> {

    EventType getPrivate();
}
