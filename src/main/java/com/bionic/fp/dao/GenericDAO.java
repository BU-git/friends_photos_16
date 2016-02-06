package com.bionic.fp.dao;

import com.bionic.fp.domain.BaseEntity;
import com.bionic.fp.exception.logic.EntityNotFoundException;

import java.io.Serializable;

/**
 * Created by boubdyk on 11.11.2015.
 */
public interface GenericDAO<T extends BaseEntity, PK extends Serializable> {
    /** Save newInstance object to DataBase
     */
    T create(T t);

    /**
     * Get object from DataBase using id like PK
     */
    T read(PK id);

    /**
     * Save changes to DataBase that has been made in object
     */
    T update(T t);

    /**
     * Delete object from DataBase
     */
    void delete(PK id);

    /**
     * Looks for entity with given id.
     * If no entity is found EntityNotFoundException is thrown.
     *
     * @param id entity unique identifier.
     * @return entity object by given id.
     * @throws EntityNotFoundException if no entity is found
     *                                 by given id.
     */
    T getOrThrow(final PK id) throws EntityNotFoundException;

    /**
     * Method is used for soft delete
     * and recover entity if it was soft deleted.
     *
     * @param id entity unique identifier.
     * @param value for isSoftDeleted parameter.
     */
    void setDeleted(final PK id, final boolean value);
}
