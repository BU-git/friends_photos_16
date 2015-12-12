package com.bionic.fp.dao.impl;

import com.bionic.fp.Constants;
import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

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
    private EntityManager em;

    public AccountDaoImpl() {}

    @Override
    public Long create(Account account) {
        em.persist(account);
        return account.getId();
    }

    @Override
    public Account read(Long accountId) {
        return em.find(Account.class, accountId);
    }

    @Override
    public Account update(Account account) {
        em.merge(account);
        return account;
    }

    @Override
    public void delete(final Long accountId) throws AccountNotFoundException {
        this.em.remove(this.getOrThrow(accountId));
    }

    @Override
    public Account addAccountEvent(final Long accountId, final AccountEvent accountEvent) throws AccountNotFoundException {
        Account account = this.getOrThrow(accountId);
        account.getEvents().add(accountEvent);
        return account;
    }

    @Override
    public Account addAccountEvent(Account account, final AccountEvent accountEvent) throws AccountNotFoundException {
        if(account != null) {
            if (account.getId() == null) {
                List<AccountEvent> accountEvents = account.getEvents();
                accountEvents.add(accountEvent);
//                account.setEvents(accountEvents);
            } else {
                account = this.addAccountEvent(account.getId(), accountEvent);
            }
        }
        return account;
    }

    @Override
    public Account getWithEvents(final Long accountId) {
        EntityGraph graph = this.em.getEntityGraph("Account.events");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(Account.class, accountId, hints);
    }

    @Override
    public List<Event> getEvents(final Long accountId) throws AccountNotFoundException {
        Account account = ofNullable(this.getWithEvents(accountId)).orElseThrow(() -> new AccountNotFoundException(accountId));
        return account.getEvents()
                    .stream()
                    .parallel()
                    .map(AccountEvent::getEvent)
                    .collect(Collectors.toList());
    }

    @Override
    public Account getByEmail(String email) throws NoResultException {
        TypedQuery<Account> result = em.createQuery(SELECT_ACCOUNT_BY_EMAIL_QUERY, Account.class);
        result.setParameter("email", email);
        return result.getSingleResult();
    }

    @Override
    public Account getByFBId(String fbId) throws NoResultException {
        TypedQuery<Account> result = em.createQuery(SELECT_ACCOUNT_BY_FBID_QUERY, Account.class);
        result.setParameter("fbId", fbId);
        return result.getSingleResult();
    }

    @Override
    public Account getByVKId(String vkId) throws NoResultException {
        TypedQuery<Account> result = em.createQuery(SELECT_ACCOUNT_BY_VKID_QUERY, Account.class);
        result.setParameter("vkId", vkId);
        return result.getSingleResult();
    }

    @Override
    public Account getByUserName(String userName) throws NoResultException {
        TypedQuery<Account> result = em.createQuery(SELECT_ACCOUNT_BY_USERNAME_QUERY, Account.class);
        result.setParameter("userName", userName);
        return result.getSingleResult();
    }

    @Override
    public List<Event> getWhereOwner(Long accountId) throws AccountNotFoundException {
        Account account = ofNullable(this.getWithEvents(accountId)).orElseThrow(() -> new AccountNotFoundException(accountId));
        List<AccountEvent> events = account.getEvents();
        List<Event> eventsWhereRoleOwner = new ArrayList<>();
        for (AccountEvent event : events) {
            if(Constants.RoleConstants.OWNER.equals(event.getRole().getId())) {
                eventsWhereRoleOwner.add(event.getEvent());
            }
        }
        return eventsWhereRoleOwner;
    }

    private Account getOrThrow(final Long accountId) throws AccountNotFoundException {
        return ofNullable(this.read(accountId)).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private <T> T getSingleResult(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }
}
