package com.bionic.fp.service;

import com.bionic.fp.dao.*;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static com.bionic.fp.util.Checks.*;
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
     * todo: return new Event
     * todo: rename create
     */
    public Long createEvent(final Long ownerId, Event event) throws InvalidParameterException, EntityNotFoundException {
        checkAccount(ownerId);
        check(event.getId() == null, "When creating an event it should not have ID");
        this.validation(event);

        Role role = this.roleDAO.getOrThrow(OWNER);
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
        checkEvent(eventId);
        return this.eventDAO.read(eventId);
    }

    /**
     * Updates an event and returns the current state of the event
     *
     * @param event the event
     * @return the current state of the event and null otherwise
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EventNotFoundException if the event doesn't exist
     * todo: maybe delete EventNotFoundException
     */
    public Event update(final Event event) throws InvalidParameterException, EventNotFoundException {
        this.validation(event);
        checkEvent(event.getId());
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
        checkEvent(eventId);
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
        checkEvent(eventId);
        this.eventDAO.setDeleted(eventId, true);
    }

    //////////////////////////////////////////////
    //                  Other                   //
    //////////////////////////////////////////////

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
        checkAccount(accountId);
        checkRole(role);
        checkEvent(eventId);

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
        checkRole(roleId);
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
     * Returns a list of events as the result of searching by name and description
     *
     * @param name the name of the event
     * @param description the description of the event
     * @return a list of events
     */
    public List<Event> get(final String name, final String description) {
        return this.eventDAO.get(name, description);
    }

    //////////////////////////////////////////////
    //                 PRIVATE                  //
    //////////////////////////////////////////////

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

}
