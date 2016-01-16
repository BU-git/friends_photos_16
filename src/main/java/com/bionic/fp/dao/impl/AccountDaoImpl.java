package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * This is implementation of {@link AccountDaoImpl}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class AccountDaoImpl extends AbstractQueryHelper implements AccountDAO {

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
    public Account getByEmail(final String email) {
        return this.getSingleResult(
                this.em.createNamedQuery(Account.GET_BY_EMAIL, Account.class).setParameter("email", email));
    }

    @Override
    public Account getByFbId(final String fbId) {
        return this.getSingleResult(
                this.em.createNamedQuery(Account.GET_BY_FB_ID, Account.class).setParameter("fbId", fbId));
    }

    @Override
    public Account getByVkId(final String vkId) {
        return this.getSingleResult(
                this.em.createNamedQuery(Account.GET_BY_VK_ID, Account.class).setParameter("vkId", vkId));
    }

    private Account getOrThrow(final Long accountId) throws AccountNotFoundException {
        return ofNullable(this.read(accountId)).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

}
