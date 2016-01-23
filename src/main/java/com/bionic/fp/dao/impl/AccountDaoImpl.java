package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
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
public class AccountDaoImpl extends GenericDaoJpaImpl<Account, Long> implements AccountDAO {

    public AccountDaoImpl() {}

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

//    @Override
//    public Account getOrThrow(final Long accountId) throws AccountNotFoundException {
//        return ofNullable(this.read(accountId)).orElseThrow(() -> new AccountNotFoundException(accountId));
//    }

}
