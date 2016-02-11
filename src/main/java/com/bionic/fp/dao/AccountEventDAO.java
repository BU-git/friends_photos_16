package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;

import java.util.List;

/**
 * Represents data access object of the account-event
 *
 * @author Sergiy Gabriel
 */
public interface AccountEventDAO extends GenericDAO<AccountEvent, Long> {

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
     * Queries an account-event with setting EAGER for all properties
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

    /**
     * Returns a list of account-events as the result of searching by event ID and role ID
     *
     * @param eventId the event ID
     * @param roleId the role ID
     * @return a list of account-events
     */
    List<AccountEvent> getByEventAndRole(Long eventId, Long roleId);

    /**
     * Returns a list of account-events as the result of searching by account ID and role ID
     *
     * @param accountId the account ID
     * @param roleId the role ID
     * @return a list of account-events
     */
    List<AccountEvent> getByAccountAndRole(Long accountId, Long roleId);

    /**
     * Returns a list of the accounts of the event
     *
     * @param eventId the event ID
     * @return a list of the accounts of the event
     */
    List<Account> getAccounts(Long eventId);

    /**
     * Returns a list of accounts as the result of searching by event ID and role ID
     *
     * @param eventId the event ID
     * @param roleId the role ID
     * @return a list of accounts
     */
    List<Account> getAccounts(Long eventId, Long roleId);

    /**
     * Returns a list of the events of the account
     *
     * @param accountId the account ID
     * @return a list of the events of the account
     */
    List<Event> getEvents(Long accountId);

    /**
     * Returns a list of events as the result of searching by account ID and role ID
     *
     * @param accountId the account ID
     * @param roleId the role ID
     * @return a list of events
     */
    List<Event> getEvents(Long accountId, Long roleId);

    /**
     * Deletes an account-event
     *
     * @param accountId the account ID
     * @param eventId the event ID
     */
    void delete(Long accountId, Long eventId);
}
