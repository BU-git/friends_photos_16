package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
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
    public Long create(Event event) {
        this.entityManager.persist(event);
        return event.getId();
    }

    @Override
    public Event read(final Long id) {
        return this.entityManager.find(Event.class, id);
    }

    @Override
    public Event update(final Event transientObject) {
        this.entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(final Long id) {
        Event event = read(id);
        if(event != null) {
            this.entityManager.remove(event);
        }
    }

    @Override
    public Event addAccountEvent(final Long eventId, final AccountEvent accountEvent) {
        Event event = getWithAccounts(eventId);
        if(event != null) {
            event.getAccounts().add(accountEvent);
        }
        return event;
    }

    @Override
    public Event addAccountEvent(Event event, final AccountEvent accountEvent) {
        if(event != null) {
            if (event.isNew()) {
                List<AccountEvent> accountEvents = event.getAccounts();
                accountEvents.add(accountEvent);
//                event.setAccounts(accountEvents);
            } else {
                event = addAccountEvent(event.getId(), accountEvent);
            }
        }
        return event;
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

    @Override
    public void setDeleted(final Long id, final boolean value) {
        Event event = read(id);
        if(event != null) {
            event.setDeleted(value);
            update(event);
        }
    }
}
