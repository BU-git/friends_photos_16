package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;

import java.util.List;

/**
 * Represents data access object of the account
 *
 * @author Sergiy Gabriel
 */
public interface AccountDAO extends GenericDAO<Account, Long> {

    /**
     * Adds an account-event to the account by account ID
     *
     * @param accountId the account ID
     * @param accountEvent the account-event
     * @return an updated account
     * @throws AccountNotFoundException if the account ID doesn't exist
     */
    Account addAccountEvent(Long accountId, AccountEvent accountEvent) throws AccountNotFoundException;

    /**
     * Adds an account-event to the account by instance of the account
     *
     * @param account the account
     * @param accountEvent the account-event
     * @return an updated account
     * @throws AccountNotFoundException if the account ID doesn't exist
     */
    Account addAccountEvent(Account account, AccountEvent accountEvent) throws AccountNotFoundException;

    /**
     * Returns an account with its events by the account ID.
     * Queries an account with setting EAGER for list of events
     *
     * @param accountId the account ID
     * @return an account with its events and null if the account doesn't exist
     */
    Account getWithEvents(Long accountId);

    /**
     * Returns a list of the events by the account ID
     *
     * @param accountId the account ID
     * @return a list of the events of the account
     * @throws AccountNotFoundException if the account doesn't exist
     */
    List<Event> getEvents(Long accountId) throws AccountNotFoundException;

    /**
     * Returns an account by email
     * @param email the user email
     * @return an account by email and null if the account doesn't exist
     */
    Account getByEmail(String email);

    /**
     * Returns an account by fb id
     * @param fbId users fb unique identifier
     * @return an account by fb id and null if the account doesn't exist
     */
    Account getByFbId(String fbId);

    /**
     * Returns an account by vk id
     * @param vkId users vk unique identifier.
     * @return an account by vk id and null if the account doesn't exist
     */
    Account getByVkId(String vkId);

}
