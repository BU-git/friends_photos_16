package com.bionic.fp.dao;

import com.bionic.fp.domain.Group;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class GroupDAO implements GenericDAO<Group, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public GroupDAO(){}

    @Override
    public Long create(Group newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Group read(Long id) {
        return entityManager.find(Group.class, id);
    }

    @Override
    public Group update(Group transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        Group group = read(persistentObjectID);
        if(group != null) {
            entityManager.remove(group);
        }
    }

    /**
     * Returns a group with its owner by the specified id.
     * Queries a group with setting EAGER for its owner
     *
     * @param id the unique identifier
     * @return a group with its owner by the specified id
     */
    public Group readWithOwner(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Group.owner");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Group.class, id, hints);
    }

    /**
     * Returns a group with its accounts by the specified id.
     * Queries a group with setting EAGER for its accounts
     *
     * @param id the unique identifier
     * @return a group with its accounts by the specified id
     */
    public Group readWithAccounts(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Group.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Group.class, id, hints);
    }

    /**
     * Returns a group with its owner and accounts by the specified id.
     * Queries a group with setting EAGER for its owner and accounts
     *
     * @param id the unique identifier
     * @return a group with its owner and accounts by the specified id
     */
    public Group readWithOwnerAndAccounts(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Group.owner&accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Group.class, id, hints);
    }

//    public List<Group> findAll() {
//        TypedQuery<Group> query = this.entityManager.createQuery("SELECT g FROM Group g", Group.class);
//        return query.getResultList();
//    }
}
