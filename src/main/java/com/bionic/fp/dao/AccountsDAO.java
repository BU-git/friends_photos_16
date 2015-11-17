package com.bionic.fp.dao;

import com.bionic.fp.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class AccountsDAO implements GenericDAO<Account, Long> {
    /**
     * JPQL query string to get account with specified email address
     */
    private static final String SELECT_ACCOUNT_BY_EMAIL = "SELECT a FROM Account a WHERE a.email = :email";

    /**
     * JPQL query string to get account with specified facebook id
     */
    private static final String SELECT_ACCOUNT_BY_FB_ID = "SELECT a FROM Account a WHERE a.facebookId = :id";

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

    /**
     * Return account with specified user email
     * @param email user email
     * @return account with such email, or null if there is no such account
     */
    public Account getAccountByEmail(String email) {
        TypedQuery<Account> query = entityManager.createQuery(SELECT_ACCOUNT_BY_EMAIL, Account.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Return account with specified facebook user id
     * @param facebookId facebook user id
     * @return account with such id, or null if there is no such account
     */
    public Account getAccountByFBId(String facebookId) {
        TypedQuery<Account> query = entityManager.createQuery(SELECT_ACCOUNT_BY_FB_ID, Account.class);
        query.setParameter("id", facebookId);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
