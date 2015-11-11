package com.bionic.fp.dao;

import com.bionic.fp.entity.Photos;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class PhotosDAO implements GenericDAO<Photos, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public PhotosDAO() {}

    @Override
    public Long create(Photos newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Photos read(Long id) {
        return entityManager.find(Photos.class, id);
    }

    @Override
    public Photos update(Photos transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
