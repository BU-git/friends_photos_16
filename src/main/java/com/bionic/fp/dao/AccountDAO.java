package com.bionic.fp.dao;

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
public class AccountDAO implements GenericDAO<Account, Long> {

    public static final String SELECT_ACCOUNT_BY_EMAIL_QUERY = "SELECT a FROM Account a WHERE a.email=:email";
    public static final String SELECT_ACCOUNT_BY_FBID_QUERY = "SELECT a FROM Account a WHERE a.fbId=:fbId";
    public static final String SELECT_ACCOUNT_BY_VKID_QUERY = "SELECT a FROM Account a WHERE a.vkId=:vkId";
    public static final String SELECT_ACCOUNT_BY_USERNAME_QUERY = "SELECT a FROM Account a WHERE a.userName=:userName";

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

    /**
     * Returns an account with its groups by the specified id.
     * Queries an account with setting EAGER for list of groups
     *
     * @param id the unique identifier
     * @return an account with its groups by the specified id
     */
    public Account readWithGroups(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Account.groupConnections");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Account.class, id, hints);
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
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_EMAIL_QUERY, Account.class);
        result.setParameter("email", email);
        return result.getSingleResult();
    }

    /**
     * Used to get account by fb id if it exist.
     * @param fbId users fb unique identifier.
     * @return instance of Account by requested fb id.
     */
    public Account getByFBId(String fbId) throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_FBID_QUERY, Account.class);
        result.setParameter("fbId", fbId);
        return result.getSingleResult();
    }

    /**
     * Used to get account by vk id if it exist.
     * @param vkId users vk unique identifier.
     * @return instance of Account by requested fb id.
     */
    public Account getByVKId(String vkId)  throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_VKID_QUERY, Account.class);
        result.setParameter("vkId", vkId);
        return result.getSingleResult();
    }

    /**
     * Used to get account by user name if it exist.
     * @param userName user name.
     * @return instance of Account by requested user name.
     */
    public Account getByUserName(String userName)  throws NoResultException {
        TypedQuery<Account> result = entityManager.createQuery(SELECT_ACCOUNT_BY_USERNAME_QUERY, Account.class);
        result.setParameter("userName", userName);
        return result.getSingleResult();
    }
}
