package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * This is implementation of {@link EventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class EventDaoImpl implements EventDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;

    public EventDaoImpl(){
    }

    @Override
    public Long create(final Event event) {
        this.em.persist(event);
        return event.getId();
    }

    @Override
    public Event read(final Long eventId) {
        return this.em.find(Event.class, eventId);
    }

    @Override
    public Event update(final Event event) {
        return this.em.merge(event);
    }

    @Override
    public void delete(final Long eventId) throws EventNotFoundException {
        Event event = this.getOrThrow(eventId);
        this.em.remove(event);
    }

    @Override
    public Event addAccountEvent(final Long eventId, final AccountEvent accountEvent) throws EventNotFoundException {
        Event event = this.getOrThrow(eventId);
        return addAccountEvent(event, accountEvent);
    }

    @Override
    public Event addAccountEvent(final Event event, final AccountEvent accountEvent) {
        event.getAccounts().add(accountEvent);
        return event;
    }

    @Override
    public Event getWithAccounts(final Long eventId) {
        EntityGraph graph = this.em.getEntityGraph("Event.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(Event.class, eventId, hints);
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
    public List<Photo> getPhotos(final Long eventId) throws EventNotFoundException {
        EntityGraph graph = this.em.getEntityGraph("Event.photos");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return ofNullable(this.em.find(Event.class, eventId, hints)).
                orElseThrow(() -> new EventNotFoundException(eventId)).getPhotos();
    }

    @Override
    public List<Comment> getComments(final Long eventId) throws EventNotFoundException {
        EntityGraph graph = this.em.getEntityGraph("Event.comments");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return ofNullable(this.em.find(Event.class, eventId, hints)).
                orElseThrow(() -> new EventNotFoundException(eventId)).getComments();
    }

    @Override
    public void setDeleted(final Long eventId, final boolean value) throws EventNotFoundException {
        Event event = this.getOrThrow(eventId);
        event.setDeleted(value);
        this.update(event);
    }

    @Override
    public boolean isOwnerLoaded(final Event event) {
        return this.em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(event, "owner");
    }

    @Override
    public Event getOrThrow(final Long eventId) throws EventNotFoundException {
        return ofNullable(this.read(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public List<Event> get(final String name, final String description) {
        if(isNotEmpty(name) && isNotEmpty(description)) {
            return this.em.createNamedQuery(Event.FIND_BY_NAME_AND_DESCRIPTION, Event.class)
                    .setParameter("name", name)
                    .setParameter("description", description)
                    .getResultList();
        }
        if(isNotEmpty(name)) {
            return this.em.createNamedQuery(Event.FIND_BY_NAME, Event.class)
                    .setParameter("name", name)
                    .getResultList();
        }
        if(isNotEmpty(description)) {
            return this.em.createNamedQuery(Event.FIND_BY_DESCRIPTION, Event.class)
                    .setParameter("description", description)
                    .getResultList();
        }
        return this.em.createNamedQuery(Event.FIND_ALL, Event.class).getResultList();
    }
}
