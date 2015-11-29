package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Entry point to perform operations over event entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class EventService {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private EventDAO eventDAO;

    @Inject
    private AccountEventDAO accountEventDAO;

    @Inject
    private RoleDAO roleDAO;

    public EventService() {}

    /**
     * Adds or updates an account to the event
     *
     * @param accountId the account ID
     * @param eventId the event ID
     * @param role the role of this account in this event
     * todo: make update test, when more roles
     */
    public void addOrUpdateAccountToEvent(final Long accountId, final Long eventId, final Role role) {
        if(accountId == null || eventId == null || role == null) {
            return;
        }

        AccountEvent conn = this.accountEventDAO.get(accountId, eventId);
        if(conn != null) {
            conn.setRole(role);
            this.accountEventDAO.update(conn);
        } else {
            // create empty connection (skeleton)
            conn = new AccountEvent();

            // add empty connection to owner and event
            Account account = this.accountDAO.addAccountEvent(accountId, conn);
            Event event = this.eventDAO.addAccountEvent(eventId, conn);

            if(account != null && event != null) {
                conn.setAccount(account);
                conn.setEvent(event);
                conn.setRole(role);
                // the magic happens above!!! This is unnecessary (the account and event use CASCADE.ALL)
//                this.accountEventDAO.create(conn);
                // this block doesn't work !?!
//                this.accountDAO.update(account);
//                this.eventDAO.update(event);
            }
        }
    }

    /**
     * Saves an event into the database and returns its ID as a result of the success
     *
     * @param ownerId the owner ID
     * @param event the event
     * @return the event ID and null otherwise
     */
    public Long createEvent(final Long ownerId, Event event) {
        if(ownerId == null || !isValid(event) || event.getId() != null) {
            return null;
        }

        Role role = this.roleDAO.getOwner();
        if(role == null) {
            // how is that?
            return null;
        }

        // create empty connection (skeleton)
        AccountEvent conn = new AccountEvent();

        // add empty connection to owner and event
        Account owner = this.accountDAO.addAccountEvent(ownerId, conn);
        if(owner == null) {
            return null;
        }
        event = this.eventDAO.addAccountEvent(event, conn);

        conn.setAccount(owner);
        conn.setEvent(event);
        conn.setRole(role);
        event.setOwner(owner);
        event.setDate(LocalDateTime.now());

        Long groupId = this.eventDAO.create(event);
        this.accountDAO.update(owner);
        return groupId;
    }

    /**
     * Removes an event physically from database
     *
     * @param eventId the event ID
     */
    public void removePhysically(final Long eventId) {
        if(eventId != null) {
            this.eventDAO.delete(eventId);
        }
    }

    /**
     * Changes the deleted field to true (soft delete)
     *
     * @param eventId the event ID
     * @return true on success and false otherwise
     */
    public boolean remove(final Long eventId) {
        return eventId != null && this.eventDAO.setDeleted(eventId, true);
    }

    /**
     * Returns an event from database by event ID and null otherwise
     *
     * @param eventId the event ID
     * @return the event and null otherwise
     */
    public Event get(final Long eventId) {
        return eventId == null ? null : this.eventDAO.read(eventId);
    }

    /**
     * Returns an event from database by event ID and null otherwise.
     * Also pulls the owner and accounts of the event
     *
     * @param eventId the event ID
     * @return the event and null otherwise
     */
    public Event getWithAccounts(final Long eventId) {
        return eventId == null ? null : this.eventDAO.getWithAccounts(eventId);
    }

    /**
     * Returns a list of the accounts of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the accounts of the event and null if the event isn't exist
     */
    public List<Account> getAccounts(final Long eventId) {
        return eventId == null ? null : this.eventDAO.getAccounts(eventId);
    }

    /**
     * Updates an event and returns the current state of the event
     *
     * @param event the event
     * @return the current state of the event and null otherwise
     */
    public Event update(final Event event) {
        if(!isValid(event) || event.getId() == null) {
            return null;
        }
        // can't change the owner!
//        if(this.eventDAO.isOwnerLoaded(event)) {      // if the owner can be LAZY then use it
        Event actual = get(event.getId());
        if (actual == null || event.getOwner() == null ||
                !Objects.equals(event.getOwner().getId(), actual.getOwner().getId())) {
            return null;
        }
        return this.eventDAO.update(event);
    }

    /**
     * Checks required parameters (should not be null) of an event
     *
     * @param event the event
     * @return true if all required parameters are initialized and false otherwise
     */
    private boolean isValid(final Event event) {
        return event != null &&
                event.getName() != null &&
                event.getEventType() != null &&
                event.getDescription() != null;
    }
}
