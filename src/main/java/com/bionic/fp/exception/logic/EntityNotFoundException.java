package com.bionic.fp.exception.logic;

import com.bionic.fp.exception.AppException;

/**
 * Signals that has not been found the entity, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class EntityNotFoundException extends AppException {

    public EntityNotFoundException(final String entityName, final Long entityId) {
        super(String.format("could not find %s '%d'.", entityName, entityId));
    }

    public EntityNotFoundException(final String entityName, final String parameter) {
        super(String.format("could not find %s '%s'.", entityName, parameter));
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }
}
