package com.bionic.fp.service;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.InvalidParameterException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

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
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public List<Account> getAccounts(final Long eventId, final Integer roleId) throws InvalidParameterException {
        check(eventId != null, "The event ID should not be null");
        check(roleId != null, "The role ID should not be null");
        return this.accountEventDAO.getAccounts(eventId, roleId);
    }

    /**
     * Returns an ID list of accounts as the result of searching by event ID and role ID
     *
     * @param eventId the event ID
     * @param roleId the role ID
     * @return an ID list of accounts
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public List<Long> getAccountIds(final Long eventId, final Integer roleId) throws InvalidParameterException {
        List<Account> accounts = this.getAccounts(eventId, roleId);
        return accounts.stream().parallel().map(Account::getId).collect(Collectors.toList());
    }

    /**
     * Returns a list of events as the result of searching by account ID and role ID
     *
     * @param accountId the account ID
     * @param roleId the role ID
     * @return a list of events
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public List<Event> getEvents(final Long accountId, final Integer roleId) throws InvalidParameterException {
        check(accountId != null, "The account ID should not be null");
        check(roleId != null, "The role ID should not be null");
        return this.accountEventDAO.getEvents(accountId, roleId);
    }

    /**
     * Returns an ID list of events as the result of searching by account ID and role ID
     *
     * @param accountId the account ID
     * @param roleId the role ID
     * @return an ID list of events
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public List<Long> getEventIds(final Long accountId, final Integer roleId) throws InvalidParameterException {
        List<Event> events = this.getEvents(accountId, roleId);
        return events.stream().parallel().map(Event::getId).collect(Collectors.toList());
    }
}
