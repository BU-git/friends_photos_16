package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventTypeDAO;
import com.bionic.fp.domain.EventType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventTypeDaoImpl implements EventTypeDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    @Override
    public Integer create(final EventType eventType) {
        this.entityManager.persist(eventType);
        return eventType.getId();
    }

    @Override
    public EventType read(final Integer id) {
        return this.entityManager.find(EventType.class, id);
    }

    @Override
    public EventType update(final EventType eventType) {
        this.entityManager.merge(eventType);
        return eventType;
    }

    @Override
    public void delete(final Integer id) {
        EventType eventType = read(id);
        if(eventType != null) {
            this.entityManager.remove(eventType);
        }
    }

    @Override
    public EventType getPrivate() {
        // todo: delete this block
        EventType eventType = new EventType();
        eventType.setTypeName("PRIVATE");
        this.entityManager.persist(eventType);
        return this.entityManager.find(EventType.class, eventType.getId());

        // todo: use it
//        return this.entityManager.find(EventType.class, 1L);
    }
}
