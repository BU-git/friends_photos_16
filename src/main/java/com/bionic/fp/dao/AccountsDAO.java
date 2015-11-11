package com.bionic.fp.dao;

import com.bionic.fp.entity.Accounts;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class AccountsDAO implements GenericDAO<Accounts, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public AccountsDAO() {}

    @Override
    public Long create(Accounts newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Accounts read(Long id) {
        return entityManager.find(Accounts.class, id);
    }

    @Override
    public Accounts update(Accounts transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
