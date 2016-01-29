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
     * Returns an account with its events by the account ID.
     * Queries an account with setting EAGER for list of events
     *
     * @param accountId the account ID
     * @return an account with its events and null if the account doesn't exist
     * todo: delete it
     */
    @Deprecated
    Account getWithEvents(Long accountId);

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
