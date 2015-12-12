package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventTypeDAO;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * This is implementation of {@link EventTypeDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventTypeDaoImpl implements EventTypeDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;

    @Override
    public Integer create(final EventType eventType) {
        this.em.persist(eventType);
        return eventType.getId();
    }

    @Override
    public EventType read(final Integer eventTypeId) {
        return this.em.find(EventType.class, eventTypeId);
    }

    @Override
    public EventType update(final EventType eventType) {
        return this.em.merge(eventType);
    }

    @Override
    public void delete(final Integer eventTypeId) throws EventTypeNotFoundException {
        EventType eventType = this.getOrThrow(eventTypeId);
        this.em.remove(eventType);
    }

    @Override
    public EventType getPrivate() throws EventTypeNotFoundException {
        return this.getOrThrow(1);
    }

    private EventType getOrThrow(final Integer eventTypeId) throws EventTypeNotFoundException {
        return ofNullable(this.read(eventTypeId)).orElseThrow(() -> new EventTypeNotFoundException(eventTypeId));
    }
}
