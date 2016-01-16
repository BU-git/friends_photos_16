package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * This is implementation of {@link AccountEventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class AccountEventDaoImpl extends AbstractQueryHelper implements AccountEventDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;


    @Override
    public Long create(AccountEvent accountEvent) {
        this.em.persist(accountEvent);
        return accountEvent.getId();
    }

    @Override
    public AccountEvent read(final Long accountEventId) {
        return this.em.find(AccountEvent.class, accountEventId);
    }

    @Override
    public AccountEvent update(final AccountEvent accountEvent) {
        this.em.merge(accountEvent);
        return accountEvent;
    }

    @Override
    public void delete(final Long accountEventId) throws AccountEventNotFoundException {
        AccountEvent accountEvent = this.getOrThrow(accountEventId);
        this.em.remove(accountEvent);
    }

    @Override
    public AccountEvent getWithAccountEvent(final Long id) {
        EntityGraph<AccountEvent> graph = this.em.createEntityGraph(AccountEvent.class);
        graph.addAttributeNodes("account", "event", "role");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(AccountEvent.class, id, hints);
    }

    @Override
    public AccountEvent get(final Long accountId, final Long eventId) {
        return this.getSingleResult(
				this.em.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_ID_AND_EVENT_ID, AccountEvent.class)
						.setParameter("accountId", accountId)
						.setParameter("eventId", eventId));
    }

    @Override
    public AccountEvent getWithAccountEvent(final Long accountId, final Long eventId) {
        EntityGraph<AccountEvent> graph = this.em.createEntityGraph(AccountEvent.class);
        graph.addAttributeNodes("account", "event", "role");
        return this.getSingleResult(
				this.em.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_ID_AND_EVENT_ID, AccountEvent.class)
						.setParameter("accountId", accountId)
						.setParameter("eventId", eventId)
						.setHint("javax.persistence.loadgraph", graph));
    }

    @Override
    public Role getRole(final Long accountId, final Long eventId) {
        AccountEvent result = this.getSingleResult(
				this.em.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_ID_AND_EVENT_ID, AccountEvent.class)
						.setParameter("accountId", accountId)
						.setParameter("eventId", eventId));
        return result == null ? null : result.getRole();
    }

    @Override
    public List<AccountEvent> getByEventAndRole(final Long eventId, final Long roleId) {
        return this.em.createNamedQuery(AccountEvent.FIND_BY_EVENT_ID_AND_ROLE_ID, AccountEvent.class)
                .setParameter("eventId", eventId)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    @Override
    public List<AccountEvent> getByAccountAndRole(final Long accountId, final Long roleId) {
        return this.em.createNamedQuery(AccountEvent.FIND_BY_ACCOUNT_ID_AND_ROLE_ID, AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    @Override
    public List<Account> getAccounts(final Long eventId, final Long roleId) {
        EntityGraph<AccountEvent> graph = this.em.createEntityGraph(AccountEvent.class);
        graph.addAttributeNodes("account");
        return this.em.createNamedQuery(AccountEvent.FIND_BY_EVENT_ID_AND_ROLE_ID, AccountEvent.class)
                .setParameter("eventId", eventId)
                .setParameter("roleId", roleId)
                .setHint("javax.persistence.loadgraph", graph)
                .getResultList().stream().parallel()
                .map(AccountEvent::getAccount)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEvents(final Long accountId, final Long roleId) {
        EntityGraph<AccountEvent> graph = this.em.createEntityGraph(AccountEvent.class);
        graph.addAttributeNodes("event");
        return this.em.createNamedQuery(AccountEvent.FIND_BY_ACCOUNT_ID_AND_ROLE_ID, AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("roleId", roleId)
                .setHint("javax.persistence.loadgraph", graph)
                .getResultList().stream().parallel()
                .map(AccountEvent::getEvent)
                .collect(Collectors.toList());
    }

    private AccountEvent getOrThrow(final Long accountEventId) throws AccountEventNotFoundException {
        return ofNullable(this.read(accountEventId)).orElseThrow(() ->
                new AccountEventNotFoundException(accountEventId));
    }

    private AccountEvent getOrThrow(final Long accountId, final Long eventId) throws AccountEventNotFoundException {
        return ofNullable(this.get(accountId, eventId)).orElseThrow(() ->
                new AccountEventNotFoundException(accountId, eventId));
    }

    private Role getRoleOrThrow(final Long accountId, final Long eventId) throws AccountEventNotFoundException {
        return this.getOrThrow(accountId, eventId).getRole();
    }
}
