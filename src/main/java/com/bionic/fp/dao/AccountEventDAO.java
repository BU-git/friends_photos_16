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
     * Returns an account-group conn with its account and group by the specified id.
     * Queries an account-group with setting EAGER for its account and group
     *
     * @param id the unique identifier
     * @return an account-group conn with its account and group by the specified id
     */
    AccountEvent getWithAccountAndEvent(Long id);

    AccountEvent getByAccountAndEventId(Long accountId, Long eventId);

    AccountEvent getByAccountAndEventIdWithAccountAndEvent(Long accountId, Long eventId);

    Role getRoleByAccountAndEventId(Long accountId, Long eventId);

}
