package com.bionic.fp.dao;

import com.bionic.fp.entity.Group;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class GroupDAO implements GenericDAO<Group, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public GroupDAO(){}

    @Override
    public Long create(Group newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Group read(Long id) {
        return entityManager.find(Group.class, id);
    }

    @Override
    public Group update(Group transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
