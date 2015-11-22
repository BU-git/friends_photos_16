package com.bionic.fp.service;

import com.bionic.fp.dao.AccountEventDaoImpl;
import com.bionic.fp.domain.AccountEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Entry point to perform operations over account-group connection entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class AccountEventService {

    @Inject
    private AccountEventDaoImpl accountEventDAO;

    public AccountEventService() {}

    public Long addAccountEvent(final AccountEvent accountGroup) {
        return accountGroup == null ? null : this.accountEventDAO.create(accountGroup);
    }

    public void removeById(final Long id) {
        if(id != null) {
            this.accountEventDAO.delete(id);
        }
    }

    public AccountEvent getById(final Long id) {
        return id == null ? null : this.accountEventDAO.read(id);
    }

    public AccountEvent getByAccountAndGroupId(final Long accountId, final Long groupId) {
        return (accountId == null || groupId == null) ? null :
                this.accountEventDAO.readByAccountAndGroupId(accountId, groupId);
    }

    public AccountEvent getByIdWithAccountAndGroup(final Long id) {
        return id == null ? null : this.accountEventDAO.readWithAccountAndGroup(id);
    }

    public AccountEvent getByAccountAndGroupIdWithAccountAndGroup(final Long accountId, final Long groupId) {
        return (accountId == null || groupId == null) ? null :
                this.accountEventDAO.readByAccountAndGroupIdWithAccountAndGroup(accountId, groupId);
    }

    public AccountEvent update(final AccountEvent accountGroup) {
        return accountGroup == null ? null : this.accountEventDAO.update(accountGroup);
    }
}
