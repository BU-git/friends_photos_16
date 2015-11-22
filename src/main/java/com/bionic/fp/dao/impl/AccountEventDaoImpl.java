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
 * todo: comment
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
    public AccountEvent getWithAccountAndGroup(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountEvent.account&event");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(AccountEvent.class, id, hints);
    }

    @Override
    public AccountEvent getByAccountAndGroupId(final Long accountId, final Long groupId) {
        return this.entityManager.createNamedQuery("findConnByAccount&Event", AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("eventId", groupId)
                .getSingleResult();
    }

    @Override
    public AccountEvent getByAccountAndGroupIdWithAccountAndGroup(final Long accountId, final Long groupId) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountEvent.account&event");
        return this.entityManager.createNamedQuery("findConnByAccount&Event", AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("eventId", groupId)
                .setHint("javax.persistence.loadgraph", graph)
                .getSingleResult();
    }

    @Override
    public Role getRoleByAccountAndGroupId(final Long accountId, final Long groupId) {
        AccountEvent result = this.entityManager.createNamedQuery("findConnByAccount&Event", AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("eventId", groupId)
                .getSingleResult();
        return result == null ? null : result.getRole();
    }
}
