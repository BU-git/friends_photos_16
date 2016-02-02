package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;

/**
 * Represents data access object of the account
 *
 * @author Sergiy Gabriel
 */
public interface AccountDAO extends GenericDAO<Account, Long> {

    /**
     * Returns an account by email
     *
     * @param email the user email
     * @return an account by email and null if the account doesn't exist
     */
    Account getByEmail(String email);

    /**
     * Returns an account by fb ID
     *
     * @param fbId users fb unique identifier
     * @return an account by fb ID and null if the account doesn't exist
     */
    Account getByFbId(String fbId);

    /**
     * Returns an account by vk ID
     *
     * @param vkId users vk unique identifier.
     * @return an account by vk ID and null if the account doesn't exist
     */
    Account getByVkId(String vkId);
}
