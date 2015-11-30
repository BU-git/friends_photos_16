package com.bionic.fp.dao;

import com.bionic.fp.exception.app.logic.EntityNotFoundException;

import java.io.Serializable;

/**
 * Created by boubdyk on 11.11.2015.
 */
public interface GenericDAO<T, PK extends Serializable> {
    /** Save newInstance object to DataBase
     */
    PK create(T newInstance);

    /**
     * Get object from DataBase using id like PK
     */
    T read(PK id);

    /**
     * Save changes to DataBase that has been made in object
     */
    T update(T transientObject);

    /**
     * Delete object from DataBase
     * @throws EntityNotFoundException if the entity ID doesn't exist
     */
    void delete(PK persistentObjectID) throws EntityNotFoundException;
}
