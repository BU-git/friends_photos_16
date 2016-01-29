package com.bionic.fp.dao;

import com.bionic.fp.domain.*;

import java.util.List;

/**
 * Represents data access object of the event
 *
 * @author Sergiy Gabriel
 */
public interface EventDAO extends GenericDAO<Event, Long> {

    /**
     * Returns an event with its accounts by the specified id.
     * Queries an event with setting EAGER for its accounts
     *
     * @param eventId the unique identifier
     * @return an event with its accounts and null if the event doesn't exist
     */
    @Deprecated
    Event getWithAccounts(Long eventId);

    /**
     * Returns a list of events as the result of searching by name and description
     *
     * @param name the name of the event
     * @param description the description of the event
     * @return a list of events
     */
    List<Event> get(String name, String description);
}
