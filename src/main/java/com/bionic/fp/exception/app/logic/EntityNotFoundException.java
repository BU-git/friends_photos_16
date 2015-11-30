package com.bionic.fp.exception.app.logic;

import com.bionic.fp.exception.app.AppException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class EntityNotFoundException extends AppException {

    public EntityNotFoundException(final String entityName, final Long entityId) {
        super(String.format("could not find %s '%d'.", entityName, entityId));
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }
}
