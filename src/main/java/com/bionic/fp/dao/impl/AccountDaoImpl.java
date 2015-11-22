package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class AccountDaoImpl implements AccountDAO {

    public static final String SELECT_ACCOUNT_BY_EMAIL_QUERY = "SELECT a FROM Account a WHERE a.email=:email";
    public static final String SELECT_ACCOUNT_BY_FBID_QUERY = "SELECT a FROM Account a WHERE a.fbId=:fbId";
    public static final String SELECT_ACCOUNT_BY_VKID_QUERY = "SELECT a FROM Account a WHERE a.vkId=:vkId";
    public static final String SELECT_ACCOUNT_BY_USERNAME_QUERY = "SELECT a FROM Account a WHERE a.userName=:userName";

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public AccountDaoImpl() {}

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

    @Override
    public Account getWithEvents(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Account.events");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Account.class, id, hints);
    }

    @Override
    public Account getByEmail(String email)  throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_EMAIL_QUERY, Account.class);
        result.setParameter("email", email);
        return result.getSingleResult();
    }

    @Override
    public Account getByFBId(String fbId) throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_FBID_QUERY, Account.class);
        result.setParameter("fbId", fbId);
        return result.getSingleResult();
    }

    @Override
    public Account getByVKId(String vkId)  throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_VKID_QUERY, Account.class);
        result.setParameter("vkId", vkId);
        return result.getSingleResult();
    }

    @Override
    public Account getByUserName(String userName)  throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_USERNAME_QUERY, Account.class);
        result.setParameter("userName", userName);
        return result.getSingleResult();
    }
}