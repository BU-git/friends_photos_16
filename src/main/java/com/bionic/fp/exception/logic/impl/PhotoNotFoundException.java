package com.bionic.fp.exception.logic.impl;

import com.bionic.fp.exception.logic.EntityNotFoundException;

/**
 * Signals that has not been found the photo, without which the subsequent business logic is not possible
 *
 * @author Sergiy Gabriel
 */
public class PhotoNotFoundException extends EntityNotFoundException {

    public PhotoNotFoundException(Long photoId) {
        super("photo", photoId);
    }

}
