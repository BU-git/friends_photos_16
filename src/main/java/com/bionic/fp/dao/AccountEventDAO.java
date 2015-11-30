package com.bionic.fp.dao;

import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Role;

/**
 * Represents data access object of the account-event
 *
 * @author Sergiy Gabriel
 */
public interface AccountEventDAO extends GenericDAO<AccountEvent, Long> {

    /**
     * Returns an account-event with its account and event by the specified id.
     * Queries an account-event with setting EAGER for its account and event
     *
     * @param id the unique identifier
     * @return an account-event with its account and null if the account-event doesn't exist
     */
    AccountEvent getWithAccountEvent(Long id);

    /**
     * Returns an account-event by the specified account ID and event ID.
     *
     * @param accountId the account ID
     * @param eventId the event ID
     * @return an account-event and null if the account-event doesn't exist
     */
    AccountEvent get(Long accountId, Long eventId);

    /**
     * Returns an account-event with its account and event by the specified account ID and event ID.
     * Queries an account-event with setting EAGER for its account and event
     *
     * @param accountId the account ID
     * @param eventId the event ID
     * @return an account-event with its account and null if the account-event doesn't exist
     */
    AccountEvent getWithAccountEvent(Long accountId, Long eventId);

    /**
     * Returns a role by the specified account ID and event ID.
     *
     * @param accountId the account ID
     * @param eventId the event ID
     * @return a role and null if the role doesn't exist
     */
    Role getRole(Long accountId, Long eventId);

}
