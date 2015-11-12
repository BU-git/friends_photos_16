package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class AccountsDAO implements GenericDAO<Account, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public AccountsDAO() {}

    @Override
    public Long create(Account newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Account read(Long id) {
        return entityManager.find(Account.class, id);
    }

    @Override
    public Account update(Account transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
