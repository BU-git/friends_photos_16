package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * This is an implementation of {@link AccountEventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
@Transactional
public class AccountEventDaoImpl extends GenericDaoJpaImpl<AccountEvent, Long> implements AccountEventDAO {

    private static final String ACCOUNT = "account";
    private static final String EVENT = "event";
    private static final String ROLE = "role";

    public AccountEventDaoImpl() {}

    @Override
    public AccountEvent get(final Long accountId, final Long eventId) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(accountId, eventId, null)));
    }

    @Override
    public AccountEvent getWithAccountEvent(final Long accountId, final Long eventId) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(accountId, eventId, null))
                .setHint(HINT_LOAD_GRAPH, getGraph(ACCOUNT, EVENT, ROLE)));
    }

    @Override
    public Role getRole(final Long accountId, final Long eventId) {
        AccountEvent result = this.get(accountId, eventId);
        return result == null ? null : result.getRole();
    }

    @Override
    public List<AccountEvent> getByEventAndRole(final Long eventId, final Long roleId) {
        return this.em.createQuery(this.getQuery(null, eventId, roleId)).getResultList();
    }

    @Override
    public List<AccountEvent> getByAccountAndRole(final Long accountId, final Long roleId) {
        return this.em.createQuery(this.getQuery(accountId, null, roleId)).getResultList();
    }

    @Override
    public List<Account> getAccounts(final Long eventId) {
        return this.em.createQuery(this.getQuery(null, eventId, null))
                .setHint(HINT_LOAD_GRAPH, getGraph(ACCOUNT))
                .getResultList().stream().parallel()
                .map(AccountEvent::getAccount)
                .collect(toList());
    }

    @Override
    public List<Account> getAccounts(final Long eventId, final Long roleId) {
        return this.em.createQuery(this.getQuery(null, eventId, roleId))
                .setHint(HINT_LOAD_GRAPH, getGraph(ACCOUNT))
                .getResultList().stream().parallel()
                .map(AccountEvent::getAccount)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEvents(final Long accountId) {
        return this.em.createQuery(this.getQuery(accountId, null, null))
                .setHint(HINT_LOAD_GRAPH, getGraph(EVENT))
                .getResultList().stream().parallel()
                .map(AccountEvent::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEvents(final Long accountId, final Long roleId) {
        return this.em.createQuery(this.getQuery(accountId, null, roleId))
                .setHint(HINT_LOAD_GRAPH, getGraph(EVENT))
                .getResultList().stream().parallel()
                .map(AccountEvent::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    public void setDeleted(Long id, boolean value) throws EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(final Long accountId, final Long eventId) throws AccountEventNotFoundException {
        AccountEvent accountEvent = this.getOrThrow(accountId, eventId);
        this.em.refresh(accountEvent);
        this.em.remove(accountEvent);
//        super.delete(accountEvent.getId());
    }

    private AccountEvent getOrThrow(final Long accountId, final Long eventId) throws AccountEventNotFoundException {
        return ofNullable(this.get(accountId, eventId)).orElseThrow(() ->
                new AccountEventNotFoundException(accountId, eventId));
    }

    private Role getRoleOrThrow(final Long accountId, final Long eventId) throws AccountEventNotFoundException {
        return this.getOrThrow(accountId, eventId).getRole();
    }

    private CriteriaQuery<AccountEvent> getQuery(final Long accountId, final Long eventId, final Long roleId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<AccountEvent> query = cb.createQuery(AccountEvent.class);

        Root<AccountEvent> accountEvent = query.from(AccountEvent.class);
        Join<AccountEvent, Event> event = accountEvent.join(EVENT);
        Join<AccountEvent, Account> account = accountEvent.join(ACCOUNT);
        Join<AccountEvent, Role> role = accountEvent.join(ROLE);

        Predicate predicate = cb.conjunction();
//        predicate = cb.and(predicate, isNotDeleted(accountEvent)); // unsupported for account-event

        if(accountId != null) {
            predicate = cb.and(predicate, equalId(account, accountId));
        }
        if(eventId != null) {
            predicate = cb.and(predicate, equalId(event, eventId));
        }
        if(roleId != null) {
            predicate = cb.and(predicate, equalId(role, roleId));
        }
        predicate = cb.and(predicate, isNotDeleted(account));
        predicate = cb.and(predicate, isNotDeleted(event));
        predicate = cb.and(predicate, isNotDeleted(role));
        return query.where(predicate);
    }

}
