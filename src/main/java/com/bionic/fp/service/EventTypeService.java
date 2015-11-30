package com.bionic.fp.service;

import com.bionic.fp.dao.EventTypeDAO;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.exception.app.logic.impl.EventTypeNotFoundException;
import com.bionic.fp.exception.app.logic.InvalidParameterException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

import static com.bionic.fp.util.Checks.check;

/**
 * Entry point to perform operations over event type entities
 *
 * @author Sergiy Gabriel
 */
@Named
@Transactional
public class EventTypeService {

    @Inject
    private EventTypeDAO eventTypeDAO;

    /**
     * Returns the private type of the event from database
     *
     * @return the private type of the event
     * @throws EventTypeNotFoundException if the event type doesn't exist
     */
    public EventType getPrivate() throws EventTypeNotFoundException {
        return this.eventTypeDAO.getPrivate();
    }

    /**
     * Returns an event type from database by event type ID
     *
     * @param eventTypeId the event type ID
     * @return the event type and null otherwise
     * @throws InvalidParameterException if the event type ID is invalid
     */
    public EventType get(final Integer eventTypeId) throws InvalidParameterException {
        this.validation(eventTypeId);
        return this.eventTypeDAO.read(eventTypeId);
    }

    /**
     * Checks required parameters of an event type
     *
     * @param eventType the event
     * @throws InvalidParameterException if incoming parameters are not valid
     */
    private void validation(final EventType eventType) throws InvalidParameterException {
        check(eventType != null, "The event type should not be null");
        check(eventType.getTypeName() != null, "The name of the event type should not be null");
    }

    /**
     * Checks an event type ID
     *
     * @param eventTypeId the event type ID
     * @throws InvalidParameterException if the event type ID is invalid
     */
    private void validation(final Integer eventTypeId) throws InvalidParameterException {
        check(eventTypeId != null, "The event type ID should not be null");
    }
}
