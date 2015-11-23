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

    public void addOrUpdateAccountToEvent(final Long accountId, final Long eventId, final Role role) {
        if(accountId == null || eventId == null || role == null) {
            return;
        }

        AccountEvent conn = this.accountEventDAO.getByAccountAndEventId(accountId, eventId);
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

                this.accountDAO.update(account);
                this.eventDAO.update(event);
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
        if(ownerId == null || event == null || !event.isNew()) {
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
     * @param id the event ID
     */
    public void removeByIdPhysically(final Long id) {
        if(id != null) {
            this.eventDAO.delete(id);
        }
    }

    /**
     * Changes the deleted field to true
     *
     * @param id the event ID
     */
    public void removeById(final Long id) {
        if(id != null) {
            this.eventDAO.setDeleted(id, true);
        }
    }

    /**
     * Returns an event from database by event ID and null otherwise
     *
     * @param id the event ID
     * @return the event and null otherwise
     */
    public Event getById(final Long id) {
        return id == null ? null : this.eventDAO.read(id);
    }

    /**
     * Returns an event from database by event ID and null otherwise.
     * Also pulls the owner of the event
     *
     * @param id the event ID
     * @return the event and null otherwise
     */
    public Event getByIdWithOwner(final Long id) {
        return id == null ? null : this.eventDAO.getWithOwner(id);
    }

    /**
     * Returns an event from database by event ID and null otherwise.
     * Also pulls the owner and accounts of the event
     *
     * @param id the event ID
     * @return the event and null otherwise
     */
    public Event getByIdWithOwnerAndAccounts(final Long id) {
        return id == null ? null : this.eventDAO.getWithOwnerAndAccounts(id);
    }

    /**
     * Updates an event and returns the current state of the event
     *
     * @param event the event
     * @return the current state of the event and null otherwise
     */
    public Event update(final Event event) {
        return event == null ? null : this.eventDAO.update(event);
    }
}
