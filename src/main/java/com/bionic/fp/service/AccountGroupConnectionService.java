package com.bionic.fp.service;

import com.bionic.fp.dao.AccountGroupConnectionDAO;
import com.bionic.fp.domain.AccountGroupConnection;
import com.bionic.fp.domain.Group;
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
public class AccountGroupConnectionService {

    @Inject
    private AccountGroupConnectionDAO accountGroupConnectionDAO;

    public AccountGroupConnectionService() {}

    public Long addAccountGroupConnection(final AccountGroupConnection accountGroup) {
        return accountGroup == null ? null : this.accountGroupConnectionDAO.create(accountGroup);
    }

    public void removeById(final Long id) {
        if(id != null) {
            this.accountGroupConnectionDAO.delete(id);
        }
    }

    public AccountGroupConnection getById(final Long id) {
        return id == null ? null : this.accountGroupConnectionDAO.read(id);
    }

    public AccountGroupConnection getByAccountAndGroupId(final Long accountId, final Long groupId) {
        return (accountId == null || groupId == null) ? null :
                this.accountGroupConnectionDAO.readByAccountAndGroupId(accountId, groupId);
    }

    public AccountGroupConnection getByIdWithAccountAndGroup(final Long id) {
        return id == null ? null : this.accountGroupConnectionDAO.readWithAccountAndGroup(id);
    }

    public AccountGroupConnection getByAccountAndGroupIdWithAccountAndGroup(final Long accountId, final Long groupId) {
        return (accountId == null || groupId == null) ? null :
                this.accountGroupConnectionDAO.readByAccountAndGroupIdWithAccountAndGroup(accountId, groupId);
    }

    public AccountGroupConnection update(final AccountGroupConnection accountGroup) {
        return accountGroup == null ? null : this.accountGroupConnectionDAO.update(accountGroup);
    }
}
