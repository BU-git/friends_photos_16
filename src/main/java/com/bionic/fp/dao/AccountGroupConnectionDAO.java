package com.bionic.fp.dao;

import com.bionic.fp.domain.AccountGroupConnection;
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
public class AccountGroupConnectionDAO implements GenericDAO<AccountGroupConnection, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;


    @Override
    public Long create(AccountGroupConnection accountGroupConnection) {
        this.entityManager.persist(accountGroupConnection);
        return accountGroupConnection.getId();
    }

    @Override
    public AccountGroupConnection read(final Long id) {
        return this.entityManager.find(AccountGroupConnection.class, id);
    }

    public AccountGroupConnection readByAccountAndGroupId(final Long accountId, final Long groupId) {
        return this.entityManager.createNamedQuery("findConnByAccount&Group", AccountGroupConnection.class)
                .setParameter("accountId", accountId)
                .setParameter("groupId", groupId)
                .getSingleResult();
    }

    public Role readRoleByAccountAndGroupId(final Long accountId, final Long groupId) {
        AccountGroupConnection result = this.entityManager.createNamedQuery("findConnByAccount&Group", AccountGroupConnection.class)
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
    public AccountGroupConnection readWithAccountAndGroup(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountGroupConnection.account&group");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(AccountGroupConnection.class, id, hints);
    }

    public AccountGroupConnection readByAccountAndGroupIdWithAccountAndGroup(final Long accountId, final Long groupId) {
        EntityGraph graph = this.entityManager.getEntityGraph("AccountGroupConnection.account&group");
        return this.entityManager.createNamedQuery("findConnByAccount&Group", AccountGroupConnection.class)
                .setParameter("accountId", accountId)
                .setParameter("groupId", groupId)
                .setHint("javax.persistence.loadgraph", graph)
                .getSingleResult();
    }

    @Override
    public AccountGroupConnection update(final AccountGroupConnection accountGroupConnection) {
        this.entityManager.merge(accountGroupConnection);
        return accountGroupConnection;
    }

    @Override
    public void delete(final Long id) {
        AccountGroupConnection accountGroupConnection = read(id);
        if(accountGroupConnection != null) {
            this.entityManager.remove(accountGroupConnection);
        }
    }


}
