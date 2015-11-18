package com.bionic.fp.dao;

import com.bionic.fp.domain.Photo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class PhotoDAO implements GenericDAO<Photo, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public PhotoDAO() {}

    @Override
    public Long create(Photo newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Photo read(Long id) {
        return entityManager.find(Photo.class, id);
    }

    @Override
    public Photo update(Photo transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
