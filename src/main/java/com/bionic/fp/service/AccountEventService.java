package com.bionic.fp.service;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.*;
import static com.bionic.fp.util.Checks.checkAccount;
import static com.bionic.fp.util.Checks.checkRole;
import static java.util.Optional.ofNullable;

/**
 * Entry point to perform operations over account-event connection entities
 *
 * @author Sergiy Gabriel
 */
@Service
@Transactional
public class AccountEventService {

    @Autowired private AccountEventDAO accountEventDAO;

    public AccountEventService() {}


    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////


    /**
     * Saves an account-event connection
     *
     * @param accountEvent the account-event connection
     * @return a saved account-event connection
     */
    public AccountEvent create(AccountEvent accountEvent) {
        checkAccountEvent(accountEvent);
        return this.accountEventDAO.create(accountEvent);
    }

    /**
     * Updates an account-event connection
     * Use to change the role of the account in the event
     *
     * @param accountEvent the account-event connection
     * @return an updated account-event connection
     */
    public AccountEvent update(final AccountEvent accountEvent) {
        checkAccountEvent(accountEvent);
        return this.accountEventDAO.update(accountEvent);
    }


    //////////////////////////////////////////////
    //                  Other                   //
    //////////////////////////////////////////////


    /**
     * Returns an account-event connection and null otherwise
     *
     * @param accountId the account id
     * @param eventId the event id
     * @return an account-event connection
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public AccountEvent get(final Long accountId, final Long eventId) throws InvalidParameterException {
        checkAccount(accountId);
        checkEvent(eventId);
        return this.accountEventDAO.get(accountId, eventId);
    }

    /**
     * Returns an account-event connection and throws the exception otherwise
     *
     * @param accountId the account id
     * @param eventId the event id
     * @return an account-event connection
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws AccountEventNotFoundException if the account-event connection is not found
     */
    public AccountEvent getOrThrow(final Long accountId, final Long eventId) throws InvalidParameterException, AccountEventNotFoundException {
        return ofNullable(this.get(accountId, eventId))
                .orElseThrow(() -> new AccountEventNotFoundException(accountId, eventId));
    }

    /**
     * Returns a list of the accounts of the event
     *
     * @param eventId the event ID
     * @return a list of the accounts of the event
     * @throws InvalidParameterException if incoming parameter is not valid
     */
    public List<Account> getAccounts(final Long eventId) throws InvalidParameterException {
        checkEvent(eventId);
        return this.accountEventDAO.getAccounts(eventId);
    }

    /**
     * Returns a list of the account ids of the event
     *
     * @param eventId the event ID
     * @return a list of the account ids of the event
     * @throws InvalidParameterException if incoming parameter is not valid
     */
    public List<Long> getAccountIds(final Long eventId) throws InvalidParameterException {
        List<Account> accounts = this.getAccounts(eventId);
        return accounts.stream().parallel().map(Account::getId).collect(Collectors.toList());
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
        checkEvent(eventId);
        checkRole(roleId);
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
     * Returns a list of the events of the account
     *
     * @param accountId the account ID
     * @return a list of the events of the account
     * @throws InvalidParameterException if incoming parameter is not valid
     */
    public List<Event> getEvents(final Long accountId) throws InvalidParameterException {
        checkAccount(accountId);
        return this.accountEventDAO.getEvents(accountId);
    }

    /**
     * Returns a list of the event ids of the account
     *
     * @param accountId the account ID
     * @return a list of the event ids of the account
     * @throws InvalidParameterException if incoming parameter is not valid
     */
    public List<Long> getEventIds(final Long accountId) throws InvalidParameterException {
        List<Event> events = this.getEvents(accountId);
        return events.stream().parallel().map(Event::getId).collect(Collectors.toList());
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
        checkAccount(accountId);
        checkRole(roleId);
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

    /**
     * Removes the account from the event
     *
     * @param eventId the event id
     * @param accountId the account id
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    public void delete(final Long eventId, final Long accountId) throws InvalidParameterException {
        checkEvent(eventId);
        checkAccount(accountId);
        this.accountEventDAO.delete(eventId, accountId);
    }
}
