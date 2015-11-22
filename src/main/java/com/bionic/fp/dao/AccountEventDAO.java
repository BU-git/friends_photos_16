package com.bionic.fp.dao;

import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Role;

/**
 * todo: comment
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
    AccountEvent getWithAccountAndGroup(Long id);

    AccountEvent getByAccountAndGroupId(Long accountId, Long groupId);

    AccountEvent getByAccountAndGroupIdWithAccountAndGroup(Long accountId, Long groupId);

    Role getRoleByAccountAndGroupId(Long accountId, Long groupId);

}
