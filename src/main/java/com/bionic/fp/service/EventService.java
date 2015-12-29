package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

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
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EntityNotFoundException if the owner and its role doesn't exist or the event doesn't exist
     * todo: make update test, when more roles
     */
    public void addOrUpdateAccountToEvent(final Long accountId, final Long eventId, final Role role,
                                          final String password) throws InvalidParameterException, EntityNotFoundException {
        check(accountId != null, "The account ID should not be null");
        check(role != null, "The role should not be null");
        this.validation(eventId);

        AccountEvent conn = this.accountEventDAO.get(accountId, eventId);
        if(conn != null) {
            if(OWNER.equals(conn.getRole().getId()) || OWNER.equals(role.getId())) {
                throw new InvalidParameterException("Not allowed to change the role for owner and setting the role of owner to someone else in the current event");
            }
            conn.setRole(role);
            this.accountEventDAO.update(conn);
        } else {
            Event event = this.eventDAO.getOrThrow(eventId);
            if(event.isPrivate() && event.getPassword() != null && !event.getPassword().equals(password)) {
                throw new InvalidParameterException("Incorrect event password");
            }
            // create empty connection (skeleton)
            conn = new AccountEvent();

            // add empty connection to owner and event
            Account account = this.accountDAO.addAccountEvent(accountId, conn);
            event = this.eventDAO.addAccountEvent(event, conn);

//            if(account != null && event != null) {
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

    /**
     * Adds or updates an account to the event
     *
     * @param accountId the account ID
     * @param eventId the event ID
     * @param roleId the role id of this account in this event
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EntityNotFoundException if the owner and its role doesn't exist or the event doesn't exist
     * todo: make update test, when more roles
     */
    public void addOrUpdateAccountToEvent(final Long accountId, final Long eventId, final Integer roleId,
                                          final String password) throws InvalidParameterException, EntityNotFoundException {
        check(roleId != null, "The role id should not be null");
        Role role = this.roleDAO.read(roleId);
        if(role == null) {
            throw new RoleNotFoundException(roleId);
        }
        this.addOrUpdateAccountToEvent(accountId, eventId, role, password);
    }

    /**
     * Saves an event into the database and returns its ID as a result of the success
     *
     * @param ownerId the owner ID
     * @param event the event
     * @return the event ID and null otherwise
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EntityNotFoundException if the owner and its role doesn't exist
     */
    public Long createEvent(final Long ownerId, Event event) throws InvalidParameterException, EntityNotFoundException {
        check(ownerId != null, "The owner ID should not be null");
        check(event.getId() == null, "When creating an event it should not have ID");
        this.validation(event);

        Role role = this.roleDAO.getOwner();

        // create empty connection (skeleton)
        AccountEvent conn = new AccountEvent();

        // add empty connection to owner and event
        Account owner = this.accountDAO.addAccountEvent(ownerId, conn);
        event = this.eventDAO.addAccountEvent(event, conn);

        conn.setAccount(owner);
        conn.setEvent(event);
        conn.setRole(role);
        event.setDate(LocalDateTime.now());

        Long groupId = this.eventDAO.create(event);
        this.accountDAO.update(owner);
        return groupId;
    }

    /**
     * Removes an event physically from database
     *
     * @param eventId the event ID
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public void removePhysically(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        this.validation(eventId);
        this.eventDAO.delete(eventId);
    }

    /**
     * Changes the deleted field to true (soft delete)
     *
     * @param eventId the event ID
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public void remove(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        this.validation(eventId);
        this.eventDAO.setDeleted(eventId, true);
    }

    /**
     * Returns an event from database by event ID and null otherwise
     *
     * @param eventId the event ID
     * @return the event and null otherwise
     * @throws InvalidParameterException if the event ID is invalid
     */
    public Event get(final Long eventId) throws InvalidParameterException {
        this.validation(eventId);
        return this.eventDAO.read(eventId);
    }

    /**
     * Returns an event from database by event ID or throw exception
     *
     * @param eventId the event ID
     * @return the event
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public Event getOrThrow(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        return ofNullable(this.get(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    /**
     * Returns an event from database by event ID and null otherwise.
     * Also pulls the owner and accounts of the event
     *
     * @param eventId the event ID
     * @return the event and null otherwise
     * @throws InvalidParameterException if the event ID is invalid
     */
    public Event getWithAccounts(final Long eventId) throws InvalidParameterException {
        this.validation(eventId);
        return this.eventDAO.getWithAccounts(eventId);
    }

    /**
     * Returns a list of the accounts of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the accounts of the event
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public List<Account> getAccounts(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        this.validation(eventId);
        return this.eventDAO.getAccounts(eventId);
    }

    /**
     * Returns a list of the photos of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the photos of the event
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public List<Photo> getPhotos(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        this.validation(eventId);
        return this.eventDAO.getPhotos(eventId);
    }

    /**
     * Returns a list of the comments of the event by the event ID
     *
     * @param eventId the event ID
     * @return a list of the comments of the event
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public List<Comment> getComments(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        this.validation(eventId);
        return this.eventDAO.getComments(eventId);
    }

    /**
     * Updates an event and returns the current state of the event
     *
     * @param event the event
     * @return the current state of the event and null otherwise
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public Event update(final Event event) throws InvalidParameterException, EventNotFoundException {
        this.validation(event);
        this.validation(event.getId());
        Event actual = this.get(event.getId());
        ofNullable(actual).orElseThrow(() -> new EventNotFoundException(event.getId()));
        return this.eventDAO.update(event);
    }

    /**
     * Returns the owner of the event
     *
     * @param eventId the event ID
     * @return the owner of the event
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     * @throws NonUniqueResultException if the event has too many owners
     */
    public Account getOwner(final Long eventId) throws InvalidParameterException, EventNotFoundException, NonUniqueResultException {
        check(eventId != null, "The event ID should not be null");
        List<Account> accounts = this.accountEventDAO.getAccounts(eventId, OWNER);
        check(!accounts.isEmpty(), new EventNotFoundException(eventId));
        check(accounts.size() == 1, new NonUniqueResultException("The event has too many owners"));
        return accounts.get(0);
    }

    /**
     * Checks required parameters of an event
     *
     * @param event the event
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    private void validation(final Event event) throws InvalidParameterException {
        check(event != null, "The event should not be null");
        check(event.getName() != null, "The name of the event should not be null");
        check(event.getEventType() != null, "The type of the event should not be null");
        check(event.getDescription() != null, "The description of the event should not be null");
    }

    /**
     * Checks an event ID
     *
     * @param eventId the event ID
     * @throws InvalidParameterException if the event ID is invalid
     */
    private void validation(final Long eventId) throws InvalidParameterException {
        check(eventId != null, "The event ID should not be null");
    }

    /**
     * Returns a list of events as the result of searching by name and description
     *
     * @param name the name of the event
     * @param description the description of the event
     * @return a list of events
     */
    public List<Event> get(final String name, final String description) {
        return this.eventDAO.get(name, description);
    }


}
