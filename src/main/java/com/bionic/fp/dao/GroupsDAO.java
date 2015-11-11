package com.bionic.fp.dao;

import com.bionic.fp.entity.Groups;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class GroupsDAO implements GenericDAO<Groups, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public GroupsDAO(){}

    @Override
    public Long create(Groups newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Groups read(Long id) {
        return entityManager.find(Groups.class, id);
    }

    @Override
    public Groups update(Groups transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
