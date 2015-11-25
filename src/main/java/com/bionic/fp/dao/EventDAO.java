package com.bionic.fp.dao;

import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;

import javax.persistence.PersistenceUnitUtil;

/**
 * Represents data access object of the event
 *
 * @author Sergiy Gabriel
 */
public interface EventDAO extends GenericDAO<Event, Long> {

    Event addAccountEvent(Long eventId, AccountEvent accountEvent);

    Event addAccountEvent(Event event, AccountEvent accountEvent);

    /**
     * Returns an event with its owner by the specified id.
     * Queries an event with setting EAGER for its owner
     *
     * @param id the unique identifier
     * @return an event with its owner by the specified id
     */
    Event getWithOwner(Long id);

    /**
     * Returns an event with its accounts by the specified id.
     * Queries an event with setting EAGER for its accounts
     *
     * @param id the unique identifier
     * @return an event with its accounts by the specified id
     */
    Event getWithAccounts(Long id);

    /**
     * Returns an event with its owner and accounts by the specified id.
     * Queries an event with setting EAGER for its owner and accounts
     *
     * @param id the unique identifier
     * @return an event with its owner and accounts by the specified id
     */
    Event getWithOwnerAndAccounts(Long id);

    /**
     * Sets the deleted field of the event by event ID.
     * And returns true on success and false otherwise
     *
     * @param id the event ID
     * @param value the value
     * @return true on success and false otherwise
     */
    boolean setDeleted(Long id, boolean value);

    /**
     * Checks that the event is loaded with its owner
     *
     * @param event the event
     * @return false if event's state has not been loaded or if
     *         its owner state has not been loaded, else true
     */
    boolean isOwnerLoaded(Event event);
}
