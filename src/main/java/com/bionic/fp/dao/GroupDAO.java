package com.bionic.fp.dao;

import com.bionic.fp.domain.Event;
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
public class GroupDAO implements GenericDAO<Event, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public GroupDAO(){}

    @Override
    public Long create(Event newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Event read(Long id) {
        return entityManager.find(Event.class, id);
    }

    @Override
    public Event update(Event transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        Event event = read(persistentObjectID);
        if(event != null) {
            entityManager.remove(event);
        }
    }

    /**
     * Returns a group with its owner by the specified id.
     * Queries a group with setting EAGER for its owner
     *
     * @param id the unique identifier
     * @return a group with its owner by the specified id
     */
    public Event readWithOwner(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.owner");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    /**
     * Returns a group with its accounts by the specified id.
     * Queries a group with setting EAGER for its accounts
     *
     * @param id the unique identifier
     * @return a group with its accounts by the specified id
     */
    public Event readWithAccounts(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

    /**
     * Returns a group with its owner and accounts by the specified id.
     * Queries a group with setting EAGER for its owner and accounts
     *
     * @param id the unique identifier
     * @return a group with its owner and accounts by the specified id
     */
    public Event readWithOwnerAndAccounts(final Long id) {
        EntityGraph graph = this.entityManager.getEntityGraph("Event.owner&accounts");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Event.class, id, hints);
    }

//    public List<Event> findAll() {
//        TypedQuery<Event> query = this.entityManager.createQuery("SELECT g FROM Event g", Event.class);
//        return query.getResultList();
//    }
}
