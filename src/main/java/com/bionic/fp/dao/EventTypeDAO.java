package com.bionic.fp.dao;

import com.bionic.fp.domain.EventType;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public interface EventTypeDAO extends GenericDAO<EventType, Integer> {

    EventType getPrivate();
}
