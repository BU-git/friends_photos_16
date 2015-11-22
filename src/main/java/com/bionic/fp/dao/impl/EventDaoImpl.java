package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.Event;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class EventDaoImpl implements EventDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public EventDaoImpl(){}

    @Override
    public Long create(Event newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Event read(Long id) {
        return entityManager.find(Event.class, id);
    }

    @Override
    public Event update(Event transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        Event event = read(persistentObjectID);
        if(event != null) {
            entityManager.remove(event);
        }
    }

    @Override
    public Event getWithOwner(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.owner");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    @Override
    public Event getWithAccounts(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    @Override
    public Event getWithOwnerAndAccounts(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.owner&accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }
}
