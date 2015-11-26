package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Event_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is implementation of {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventDaoImpl implements EventDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public EventDaoImpl(){
    }

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
    public Event update(final Event event) {
        return this.entityManager.merge(event);
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
            if (event.getId() == null) {
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
        EntityGraph<Event> graph = this.entityManager.createEntityGraph(Event.class);
        graph.addAttributeNodes(Event_.owner);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    @Override
    public Event getWithAccounts(final Long id) {
        EntityGraph<Event> graph = this.entityManager.createEntityGraph(Event.class);
        graph.addAttributeNodes(Event_.accounts);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    @Override
    public Event getWithOwnerAndAccounts(final Long id) {
        EntityGraph<Event> graph = this.entityManager.createEntityGraph(Event.class);
        graph.addAttributeNodes(Event_.owner);
        graph.addAttributeNodes(Event_.accounts);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    @Override
    public boolean setDeleted(final Long id, final boolean value) {
        Event event = read(id);
        if(event == null) {
            return false;
        }
        event.setDeleted(value);
        update(event);
        return true;
    }

    @Override
    public boolean isOwnerLoaded(final Event event) {
        return this.entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(event, "owner");
    }
}
