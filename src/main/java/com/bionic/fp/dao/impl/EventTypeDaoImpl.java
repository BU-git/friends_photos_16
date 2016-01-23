package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventTypeDAO;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * This is implementation of {@link EventTypeDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventTypeDaoImpl extends GenericDaoJpaImpl<EventType, Long> implements EventTypeDAO {

    public EventTypeDaoImpl() {}

    @Override
    public EventType getPrivate() throws EventTypeNotFoundException {
        return this.getOrThrow(1L);
    }

//    private EventType getOrThrow(final Long eventTypeId) throws EventTypeNotFoundException {
//        return ofNullable(this.read(eventTypeId)).orElseThrow(() -> new EventTypeNotFoundException(eventTypeId));
//    }
}
