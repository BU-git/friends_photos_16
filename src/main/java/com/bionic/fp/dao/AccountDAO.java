package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;

import javax.persistence.NoResultException;
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
     * Returns a list of the events of the account by the event ID
     *
     * @param accountId the account ID
     * @return a list of the events of the account
     * @throws AccountNotFoundException if the account ID doesn't exist
     */
    List<Event> getEvents(Long accountId) throws AccountNotFoundException;

    /**
     * This method is used to get account by email if it exist.
     * @param email user email.
     * @return instance of Account by requested email.
     */
    Account getByEmail(String email) throws NoResultException;

    /**
     * Used to get account by fb id if it exist.
     * @param fbId users fb unique identifier.
     * @return instance of Account by requested fb id.
     */
    Account getByFBId(String fbId) throws NoResultException;

    /**
     * Used to get account by vk id if it exist.
     * @param vkId users vk unique identifier.
     * @return instance of Account by requested fb id.
     */
    Account getByVKId(String vkId) throws NoResultException;

    /**
     * Used to get account by user name if it exist.
     * @param userName user name.
     * @return instance of Account by requested user name.
     */
    Account getByUserName(String userName) throws NoResultException;

    /**
     * Used to get all events where this account is owner
     * @param accountId
     * @return list of events
     */
    List<Event> getWhereOwner(Long accountId) throws AccountNotFoundException;
}
