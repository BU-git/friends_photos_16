package com.bionic.fp.exception.rest;

import com.bionic.fp.exception.AppException;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class NotFoundException extends AppException {

    public NotFoundException(final Long entityId) {
        super(String.format("could not find '%d'.", entityId));
    }
}