package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.AppException;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static com.bionic.fp.util.Checks.check;
import static com.bionic.fp.util.Checks.checkNotNull;
import static java.util.Optional.ofNullable;

/**
 * Entry point to perform operations over event entities
 *
 * @author Sergiy Gabriel
 */
@Service
@Transactional
public class EventService {

    @Autowired private AccountDAO accountDAO;
    @Autowired private EventDAO eventDAO;
    @Autowired private AccountEventDAO accountEventDAO;
    @Autowired private RoleDAO roleDAO;

    public EventService() {}

    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////

    /**
     * Saves an event into the database and returns its ID as a result of the success
     *
     * @param ownerId the owner ID
     * @param event the event
     * @return the event ID and null otherwise
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EntityNotFoundException if the owner and its role doesn't exist
     * todo: simplify this logic using accountEventDAO
     */
    public Long createEvent(final Long ownerId, Event event) throws InvalidParameterException, EntityNotFoundException {
        check(ownerId != null, "The owner ID should not be null");
        check(event.getId() == null, "When creating an event it should not have ID");
        this.validation(event);

        Role role = this.roleDAO.getOwner();

//        // create empty connection (skeleton)
//        AccountEvent conn = new AccountEvent();
//
//        // add empty connection to owner and event
//        Account owner = this.accountDAO.addAccountEvent(ownerId, conn);
//        event = this.eventDAO.addAccountEvent(event, conn);
//
//        conn.setAccount(owner);
//        conn.setEvent(event);
//        conn.setRole(role);
////        event.setDate(LocalDateTime.now()); // todo: checkout
//
//        Long eventId = this.eventDAO.create(event);
//        this.accountDAO.update(owner);
//        return eventId;

        Account owner = this.accountDAO.getOrThrow(ownerId);
        this.eventDAO.create(event);
        AccountEvent conn = new AccountEvent(event, owner, role);
        this.accountEventDAO.create(conn);
        event.getAccounts().add(conn);
        return event.getId();
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
     * Removes an event from the database
     *
     * @param eventId the event ID
     * @throws InvalidParameterException if the event ID is invalid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public void delete(final Long eventId) throws InvalidParameterException, EventNotFoundException {
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
    public void softDelete(final Long eventId) throws InvalidParameterException, EventNotFoundException {
        this.validation(eventId);
        this.eventDAO.setDeleted(eventId, true);
    }




    /**
     * Adds or updates an account to the event
     *
     * @param accountId the account ID
     * @param eventId the event ID
     * @param role the role of this account in this event
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EntityNotFoundException if the owner and its role doesn't exist or the event doesn't exist
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
            // Check out the password if the event is private
            if(event.isPrivate() && event.getPassword() != null && !event.getPassword().equals(password)) {
                throw new InvalidParameterException("Incorrect event password");
            }
            Account account = this.accountDAO.getOrThrow(accountId);
            conn = new AccountEvent(event, account, role);
            this.accountEventDAO.create(conn);
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
    public void addOrUpdateAccountToEvent(final Long accountId, final Long eventId, final Long roleId,
                                          final String password) throws InvalidParameterException, EntityNotFoundException {
        check(roleId != null, "The role id should not be null");
        Role role = ofNullable(this.roleDAO.read(roleId)).orElseThrow(() -> new RoleNotFoundException(roleId));
        this.addOrUpdateAccountToEvent(accountId, eventId, role, password);
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
     * Returns a list of events as the result of searching by name and description
     *
     * @param name the name of the event
     * @param description the description of the event
     * @return a list of events
     */
    public List<Event> get(final String name, final String description) {
        return this.eventDAO.get(name, description);
    }

    /**
     * todo: update(event)?!? fixme using commentDao! and event => eventId
     * Adds comment to event
     *
     * @param event the event
     * @param comment the comment
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public void addComment(final Event event, final Comment comment) throws InvalidParameterException, EventNotFoundException {
        checkNotNull(event, "event");
        checkNotNull(comment, "comment");

        if (event.getComments() == null) {
            throw new AppException("Invalid event entity");
        }

        if(comment.getText().isEmpty()) {
            throw new AppException("Comment is empty");
        }
        event.getComments().add(comment);
        update(event);
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
}
