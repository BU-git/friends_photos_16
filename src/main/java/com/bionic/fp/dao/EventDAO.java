package com.bionic.fp.dao;

import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;

import java.util.List;

/**
 * Represents data access object of the event
 *
 * @author Sergiy Gabriel
 */
public interface EventDAO extends GenericDAO<Event, Long> {

    /**
     * Adds an account-event to the event by event ID
     *
     * @param eventId the event ID
     * @param accountEvent the account-event
     * @return an updated event
     * @throws EventNotFoundException if the event doesn't exist
     */
    Event addAccountEvent(Long eventId, AccountEvent accountEvent) throws EventNotFoundException;

    /**
     * Adds an account-event to the event by instance of the event
     *
     * @param event the event
     * @param accountEvent the account-event
     * @return an updated event
     */
    Event addAccountEvent(Event event, AccountEvent accountEvent);

    /**
     * Returns an event with its accounts by the specified id.
     * Queries an event with setting EAGER for its accounts
     *
     * @param eventId the unique identifier
     * @return an event with its accounts and null if the event doesn't exist
     */
    Event getWithAccounts(Long eventId);

    /**
     * Returns a list of the accounts of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the accounts of the event
     * @throws EventNotFoundException if the event doesn't exist
     */
    List<Account> getAccounts(Long eventId) throws EventNotFoundException;

    /**
     * Returns a list of the photos of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the photos of the event
     * @throws EventNotFoundException if the event doesn't exist
     */
    List<Photo> getPhotos(Long eventId) throws EventNotFoundException;

    /**
     * Returns a list of the comments of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the comments of the event
     * @throws EventNotFoundException if the event doesn't exist
     */
    List<Comment> getComments(Long eventId) throws EventNotFoundException;

    /**
     * Sets the deleted field of the event by event ID.
     * And returns true on success and false otherwise
     *
     * @param eventId the event ID
     * @param value the value
     * @throws EventNotFoundException if the event doesn't exist
     */
    void setDeleted(Long eventId, boolean value) throws EventNotFoundException;

    /**
     * Checks that the event is loaded with its owner
     *
     * @param event the event
     * @return false if event's state has not been loaded or if
     *         its owner state has not been loaded, else true
     */
    boolean isOwnerLoaded(Event event);

    /**
     * Returns an event by the specified id and throws the exception otherwise
     *
     * @param eventId the event ID
     * @return an event
     * @throws EventNotFoundException if the event doesn't exist
     */
    Event getOrThrow(Long eventId) throws EventNotFoundException;
}
