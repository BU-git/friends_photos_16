package com.bionic.fp.service;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import org.springframework.stereotype.Service;
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
@Service
@Transactional
public class AccountEventService {

    @Inject
    private AccountEventDAO accountEventDAO;

    public AccountEventService() {}

    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////

    public AccountEvent create(AccountEvent accountEvent) {
        this.accountEventDAO.create(accountEvent);
        return accountEvent;
    }

    public AccountEvent get(final Long id) {
        return id == null ? null : this.accountEventDAO.read(id);
    }

    public AccountEvent update(final AccountEvent accountEvent) {
        return accountEvent == null ? null : this.accountEventDAO.update(accountEvent);
    }

    public void softDelete(final Long accountId) {
            this.accountEventDAO.setDeleted(accountId, true);
    }

    //    @Admin
    public void delete(final Long accountId) {
            this.accountEventDAO.delete(accountId);
    }

    //////////////////////////////////////////////
    //                  Other                   //
    //////////////////////////////////////////////

    public AccountEvent get(final Long accountId, final Long eventId) {
        if(accountId == null || eventId == null) {
            throw new InvalidParameterException("Not valid account or event");
        }
        AccountEvent accountEvent = this.accountEventDAO.get(accountId, eventId);
        if(accountEvent == null) {
            throw new EntityNotFoundException("You are not a member of this event");
        }
        return accountEvent;
    }

    public AccountEvent getWithAccountEvent(final Long id) {
        return id == null ? null : this.accountEventDAO.getWithAccountEvent(id);
    }

    public AccountEvent getWithAccountEvent(final Long accountId, final Long eventId) {
        return (accountId == null || eventId == null) ? null :
                this.accountEventDAO.getWithAccountEvent(accountId, eventId);
    }

    /**
     * Returns a list of accounts as the result of searching by event ID and role ID
     *
     * @param eventId the event ID
     * @param roleId the role ID
     * @return a list of accounts
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public List<Account> getAccounts(final Long eventId, final Long roleId) throws InvalidParameterException {
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
    public List<Long> getAccountIds(final Long eventId, final Long roleId) throws InvalidParameterException {
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
    public List<Event> getEvents(final Long accountId, final Long roleId) throws InvalidParameterException {
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
    public List<Long> getEventIds(final Long accountId, final Long roleId) throws InvalidParameterException {
        List<Event> events = this.getEvents(accountId, roleId);
        return events.stream().parallel().map(Event::getId).collect(Collectors.toList());
    }
}
