package com.bionic.fp.service;

import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.dao.AccountEventDAO;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Entry point to perform operations over account-event connection entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class AccountEventService {

    @Inject
    private AccountEventDAO accountEventDAO;

    public AccountEventService() {}

    public Long addAccountEvent(final AccountEvent accountEvent) {
        return accountEvent == null ? null : this.accountEventDAO.create(accountEvent);
    }

    public void removeById(final Long id) {
        if(id != null) {
            this.accountEventDAO.delete(id);
        }
    }

    public AccountEvent get(final Long id) {
        return id == null ? null : this.accountEventDAO.read(id);
    }

    public AccountEvent get(final Long accountId, final Long eventId) {
        return (accountId == null || eventId == null) ? null :
                this.accountEventDAO.get(accountId, eventId);
    }

    public AccountEvent getWithAccountEvent(final Long id) {
        return id == null ? null : this.accountEventDAO.getWithAccountEvent(id);
    }

    public AccountEvent getWithAccountEvent(final Long accountId, final Long eventId) {
        return (accountId == null || eventId == null) ? null :
                this.accountEventDAO.getWithAccountEvent(accountId, eventId);
    }

    public AccountEvent update(final AccountEvent accountEvent) {
        return accountEvent == null ? null : this.accountEventDAO.update(accountEvent);
    }
}
