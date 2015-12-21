package com.bionic.fp.service;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.Event;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.bionic.fp.util.Checks.check;

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

    /**
     * Returns a list of accounts as the result of searching by event ID and role ID
     *
     * @param eventId the event ID
     * @param roleId the role ID
     * @return a list of accounts
     */
    public List<Account> getAccounts(final Long eventId, final Integer roleId) {
        check(eventId != null, "The event ID should not be null");
        check(roleId != null, "The role ID should not be null");
        return this.accountEventDAO.getAccounts(eventId, roleId);
    }

    /**
     * Returns a list of events as the result of searching by account ID and role ID
     *
     * @param accountId the account ID
     * @param roleId the role ID
     * @return a list of events
     */
    public List<Event> getEvents(final Long accountId, final Integer roleId) {
        check(accountId != null, "The account ID should not be null");
        check(roleId != null, "The role ID should not be null");
        return this.accountEventDAO.getEvents(accountId, roleId);
    }
}
