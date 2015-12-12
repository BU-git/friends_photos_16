package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * This is implementation of {@link AccountEventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class AccountEventDaoImpl implements AccountEventDAO {

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
        EntityGraph graph = this.em.getEntityGraph("AccountEvent.full");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(AccountEvent.class, id, hints);
    }

    @Override
    public AccountEvent get(final Long accountId, final Long groupId) {
        return this.getSingleResult(
				this.em.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID, AccountEvent.class)
						.setParameter("accountId", accountId)
						.setParameter("eventId", groupId));
    }

    @Override
    public AccountEvent getWithAccountEvent(final Long accountId, final Long groupId) {
        EntityGraph graph = this.em.getEntityGraph("AccountEvent.full");
        return this.getSingleResult(
				this.em.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID, AccountEvent.class)
						.setParameter("accountId", accountId)
						.setParameter("eventId", groupId)
						.setHint("javax.persistence.loadgraph", graph));
    }

    @Override
    public Role getRole(final Long accountId, final Long groupId) {
        AccountEvent result = this.getSingleResult(
				this.em.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID, AccountEvent.class)
						.setParameter("accountId", accountId)
						.setParameter("eventId", groupId));
        return result == null ? null : result.getRole();
    }

    private <T> T getSingleResult(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {}
        return null;
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
