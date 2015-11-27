package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;

import javax.persistence.NoResultException;

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
     */
    Account addAccountEvent(Long accountId, AccountEvent accountEvent);

    /**
     * Adds an account-event to the account by instance of the account
     *
     * @param account the account
     * @param accountEvent the account-event
     * @return an updated account
     */
    Account addAccountEvent(Account account, AccountEvent accountEvent);

    /**
     * Returns an account with its groups by the specified id.
     * Queries an account with setting EAGER for list of events
     *
     * @param id the unique identifier
     * @return an account with its events by the specified id
     */
    Account getWithEvents(Long id);

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
    Account getByVKId(String vkId)  throws NoResultException;

    /**
     * Used to get account by user name if it exist.
     * @param userName user name.
     * @return instance of Account by requested user name.
     */
    Account getByUserName(String userName)  throws NoResultException;
}
