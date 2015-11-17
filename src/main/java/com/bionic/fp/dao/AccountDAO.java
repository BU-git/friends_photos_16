package com.bionic.fp.dao;

import com.bionic.fp.entity.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class AccountDAO implements GenericDAO<Account, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public AccountDAO() {}

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


    /**
     * This method is used to get account by email if it exist.
     * @param email user email.
     * @return instance of Account by requested email.
     */
    public Account getByEmail(String email)  throws NoResultException {
        String query = "SELECT a FROM Account a WHERE a.email=:email";
        TypedQuery<Account> result = entityManager.createQuery(query, Account.class);
        result.setParameter("email", email);
        return result.getSingleResult();
    }

    /**
     * Used to get account by fb id if it exist.
     * @param fbID users fb unique identifier.
     * @return instance of Account by requested fb id.
     */
    public Account getByFB(String fbID) throws NoResultException {
        String query = "SELECT a FROM Account a WHERE a.fbID=:fbID";
        TypedQuery<Account> result = entityManager.createQuery(query, Account.class);
        result.setParameter("fbID", fbID);
        return result.getSingleResult();
    }

    /**
     * Used to get account by vk id if it exist.
     * @param vkID users vk unique identifier.
     * @return instance of Account by requested fb id.
     */
    public Account getByVK(String vkID)  throws NoResultException {
        String query = "SELECT a FROM Account a WHERE a.vkID=:vkID";
        TypedQuery<Account> result = entityManager.createQuery(query, Account.class);
        result.setParameter("vkID", vkID);
        return result.getSingleResult();
    }

    /**
     * Used to get account by user name if it exist.
     * @param userName user name.
     * @return instance of Account by requested user name.
     */
    public Account getByUserName(String userName)  throws NoResultException {
        String query = "SELECT a FROM Account a WHERE a.userName=:userName";
        TypedQuery<Account> result = entityManager.createQuery(query, Account.class);
        result.setParameter("userName", userName);
        return result.getSingleResult();
    }
}
