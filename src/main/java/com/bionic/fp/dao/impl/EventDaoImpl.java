package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Event read(final Long eventId) {
        return this.entityManager.find(Event.class, eventId);
    }

    @Override
    public Event update(final Event event) {
        return this.entityManager.merge(event);
    }

    @Override
    public void delete(final Long eventId) {
        Event event = read(eventId);
        if(event != null) {
            this.entityManager.remove(event);
        }
    }

    @Override
    public Event addAccountEvent(final Long eventId, final AccountEvent accountEvent) {
        Event event = read(eventId);
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
    public Event getWithAccounts(final Long eventId) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, eventId, hints);
    }

    @Override
    public List<Account> getAccounts(final Long eventId) {
        Event event = getWithAccounts(eventId);
        return event == null ? null :
                event.getAccounts()
                    .stream()
                    .parallel()
                    .map(AccountEvent::getAccount)
                    .collect(Collectors.toList());
    }

    @Override
    public boolean setDeleted(final Long eventId, final boolean value) {
        Event event = read(eventId);
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
