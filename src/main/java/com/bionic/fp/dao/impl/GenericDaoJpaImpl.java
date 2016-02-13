package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.GenericDAO;
import com.bionic.fp.domain.BaseEntity;
import com.bionic.fp.domain.IdEntity;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * This is an implementation of {@link GenericDAO} and some general methods for working with JPA
 *
 * @author Sergiy Gabriel
 */
@Transactional
public class GenericDaoJpaImpl<T extends BaseEntity & IdEntity<PK>, PK extends Serializable> implements GenericDAO<T, PK> {

    protected static final String HINT_LOAD_GRAPH = "javax.persistence.loadgraph";
    private static final String ID = "id";
    private static final String DELETED = "deleted";

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

    public GenericDaoJpaImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T create(T t) {
        t.setCreated(LocalDateTime.now());
        this.em.persist(t);
        return t;
    }

    @Override
    @Transactional(readOnly = true)
    public T read(PK id) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(ID, id)));
    }

    @Override
    public T update(T t) {
        t.setModified(LocalDateTime.now());
        this.em.merge(t);
        return t;
    }

    @Override
    public void delete(PK id) {
        ofNullable(this.get(id)).ifPresent(entity -> {
            this.em.refresh(entity); // workaround, forcing JPA to populate all relationships so that they can be correctly removed
            this.em.remove(entity);
        });
    }

    /**
     * Returns a single result or null
     *
     * @param query the query
     * @return a single result or null
     */
    protected <E> E getSingleResult(final TypedQuery<E> query) throws NonUniqueResultException {
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    /**
     * Returns an entity by the specified id and throws the exception otherwise
     *
     * @param id the entity id
     * @return an account
     * @throws EntityNotFoundException if the entity doesn't exist
     */
    @Transactional(readOnly = true)
    public T getOrThrow(final PK id) throws EntityNotFoundException {
        return ofNullable(this.read(id)).orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    /**
     * Sets the deleted field of the entity by entity id.
     *
     * @param id the entity id
     * @param value the value
     */
    public void setDeleted(final PK id, final boolean value) {
        ofNullable(this.get(id)).ifPresent(entity -> {
            if(entity.isDeleted() != value) {
                entity.setDeleted(value);
                this.update(entity);
            }
        });
    }

    /**
     * Returns the entity graph by the attributes of the entity
     *
     * @param attributeName the attribute names of the entity
     * @return the entity graph
     */
    protected EntityGraph<T> getGraph(final String ... attributeName) {
        EntityGraph<T> graph = em.createEntityGraph(entityClass);
        graph.addAttributeNodes(attributeName);
        return graph;
    }

    /**
     * Returns the hints for EAGER loading of the attributes of the entity
     *
     * @param attributeName the attribute names of the entity
     * @return the hints
     */
    protected Map<String, Object> getHints(final String ... attributeName) {
        Map<String, Object> hints = new HashMap<>();
        hints.put(HINT_LOAD_GRAPH, getGraph(attributeName));
        return hints;
    }

    /**
     * Returns the predicate that checks the entity is not deleted
     *
     * @param entity the entity
     * @return the predicate
     */
    protected <S extends BaseEntity, X extends BaseEntity> Predicate isNotDeleted(final From<S, X> entity) {
        return this.em.getCriteriaBuilder().isFalse(entity.get(DELETED));
    }

    /**
     * Returns the predicate that checks the entity has the specified ID
     *
     * @param entity the entity
     * @param id the entity ID
     * @return the predicate
     */
    protected <S extends IdEntity<PK>, X extends IdEntity<PK>> Predicate equalId(final From<S, X> entity, final PK id) {
        return this.em.getCriteriaBuilder().equal(entity.get(ID), id);
    }

    /**
     * Returns a query for the general case when this entity is not deleted
     * and its property equivalent to the value
     *
     * @param propertyName the property name of the entity
     * @param value the value of the property
     * @return a query
     */
    protected CriteriaQuery<T> getQuery(final String propertyName, final Object value) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> entity = query.from(entityClass);
        return query.where(cb.and(isNotDeleted(entity), cb.equal(entity.get(propertyName), value)));
    }

    private T get(PK id) {
        return this.em.find(entityClass, id);
    }
}
