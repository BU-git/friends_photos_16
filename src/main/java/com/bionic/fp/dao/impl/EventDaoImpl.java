package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.app.logic.impl.EventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

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
    public Long create(final Event event) {
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
    public void delete(final Long eventId) throws EventNotFoundException {
        Event event = this.getOrThrow(eventId);
        this.entityManager.remove(event);
    }

    @Override
    public Event addAccountEvent(final Long eventId, final AccountEvent accountEvent) throws EventNotFoundException {
        Event event = this.getOrThrow(eventId);
        event.getAccounts().add(accountEvent);
        return event;
    }

    @Override
    public Event addAccountEvent(Event event, final AccountEvent accountEvent) throws EventNotFoundException {
        if (event.getId() == null) {
            List<AccountEvent> accountEvents = event.getAccounts();
            accountEvents.add(accountEvent);
//            event.setAccounts(accountEvents);
        } else {
            event = this.addAccountEvent(event.getId(), accountEvent);
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
    public List<Account> getAccounts(final Long eventId) throws EventNotFoundException {
        Event event = ofNullable(this.getWithAccounts(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
        return event.getAccounts()
                    .stream()
                    .parallel()
                    .map(AccountEvent::getAccount)
                    .collect(Collectors.toList());
    }

    @Override
    public void setDeleted(final Long eventId, final boolean value) throws EventNotFoundException {
        Event event = this.getOrThrow(eventId);
        event.setDeleted(value);
        this.update(event);
    }

    @Override
    public boolean isOwnerLoaded(final Event event) {
        return this.entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(event, "owner");
    }

    private Event getOrThrow(final Long eventId) throws EventNotFoundException {
        return ofNullable(this.read(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }
}
