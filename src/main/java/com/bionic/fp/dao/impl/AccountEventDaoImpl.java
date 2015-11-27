package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * This is implementation of {@link AccountEventDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class AccountEventDaoImpl implements AccountEventDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;


    @Override
    public Long create(AccountEvent accountGroupConnection) {
        this.entityManager.persist(accountGroupConnection);
        return accountGroupConnection.getId();
    }

    @Override
    public AccountEvent read(final Long id) {
        return this.entityManager.find(AccountEvent.class, id);
    }

    @Override
    public AccountEvent update(final AccountEvent accountGroupConnection) {
        this.entityManager.merge(accountGroupConnection);
        return accountGroupConnection;
    }

    @Override
    public void delete(final Long id) {
        AccountEvent accountGroupConnection = read(id);
        if(accountGroupConnection != null) {
            this.entityManager.remove(accountGroupConnection);
        }
    }

    @Override
    public AccountEvent getWithAccountEvent(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountEvent.full");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(AccountEvent.class, id, hints);
    }

    @Override
    public AccountEvent get(final Long accountId, final Long groupId) {
        return this.entityManager.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID, AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("eventId", groupId)
                .getSingleResult();
    }

    @Override
    public AccountEvent getWithAccountEvent(final Long accountId, final Long groupId) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountEvent.full");
        return this.entityManager.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID, AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("eventId", groupId)
                .setHint("javax.persistence.loadgraph", graph)
                .getSingleResult();
    }

    @Override
    public Role getRole(final Long accountId, final Long groupId) {
        AccountEvent result = this.entityManager.createNamedQuery(AccountEvent.GET_BY_ACCOUNT_AND_EVENT_ID, AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("eventId", groupId)
                .getSingleResult();
        return result == null ? null : result.getRole();
    }
}
