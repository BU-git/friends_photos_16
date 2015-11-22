package com.bionic.fp.dao;

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
public class AccountEventDAO implements GenericDAO<AccountEvent, Long> {

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

    public AccountEvent readByAccountAndGroupId(final Long accountId, final Long groupId) {
        return this.entityManager.createNamedQuery("findConnByAccount&Event", AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("groupId", groupId)
                .getSingleResult();
    }

    public Role readRoleByAccountAndGroupId(final Long accountId, final Long groupId) {
        AccountEvent result = this.entityManager.createNamedQuery("findConnByAccount&Event", AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("groupId", groupId)
                .getSingleResult();
        return result == null ? null : result.getRole();
    }

    /**
     * Returns an account-group conn with its account and group by the specified id.
     * Queries an account-group with setting EAGER for its account and group
     *
     * @param id the unique identifier
     * @return an account-group conn with its account and group by the specified id
     */
    public AccountEvent readWithAccountAndGroup(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountEvent.account&group");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(AccountEvent.class, id, hints);
    }

    public AccountEvent readByAccountAndGroupIdWithAccountAndGroup(final Long accountId, final Long groupId) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountEvent.account&group");
        return this.entityManager.createNamedQuery("findConnByAccount&Event", AccountEvent.class)
                .setParameter("accountId", accountId)
                .setParameter("groupId", groupId)
                .setHint("javax.persistence.loadgraph", graph)
                .getSingleResult();
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


}
