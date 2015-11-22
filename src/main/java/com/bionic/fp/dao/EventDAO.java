package com.bionic.fp.dao;

import com.bionic.fp.domain.Event;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public interface EventDAO extends GenericDAO<Event, Long> {

    /**
     * Returns a group with its owner by the specified id.
     * Queries a group with setting EAGER for its owner
     *
     * @param id the unique identifier
     * @return a group with its owner by the specified id
     */
    Event getWithOwner(Long id);

    /**
     * Returns a group with its accounts by the specified id.
     * Queries a group with setting EAGER for its accounts
     *
     * @param id the unique identifier
     * @return a group with its accounts by the specified id
     */
    Event getWithAccounts(Long id);

    /**
     * Returns a group with its owner and accounts by the specified id.
     * Queries a group with setting EAGER for its owner and accounts
     *
     * @param id the unique identifier
     * @return a group with its owner and accounts by the specified id
     */
    Event getWithOwnerAndAccounts(Long id);
}
