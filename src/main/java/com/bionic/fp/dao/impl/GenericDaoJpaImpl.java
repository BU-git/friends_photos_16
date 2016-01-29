package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.GenericDAO;
import com.bionic.fp.domain.BaseEntity;
import com.bionic.fp.domain.IdEntity;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;

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
public class GenericDaoJpaImpl<T extends BaseEntity & IdEntity<PK>, PK extends Serializable> implements GenericDAO<T, PK> {

    protected static final String HINT_LOAD_GRAPH = "javax.persistence.loadgraph";
    private static final String FIELD_ID = "id";
    private static final String FIELD_DELETED = "deleted";

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
    public T read(PK id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> entity = query.from(entityClass);
        query.where(cb.and(isNotDeleted(entity), equalId(entity, id)));
        return this.getSingleResult(this.em.createQuery(query));
    }

    @Override
    public T update(T t) {
        t.setModified(LocalDateTime.now());
        this.em.merge(t);
        return t;
    }

    @Override
    public void delete(PK id) throws EntityNotFoundException {
        T t = ofNullable(this.get(id)).orElseThrow(() -> new EntityNotFoundException(id.toString()));
        this.em.refresh(t); // workaround, forcing JPA to populate all relationships so that they can be correctly removed
        this.em.remove(t);
    }

    /**
     * Returns a single result or null
     *
     * @param query the query
     * @return a single result or null
     * @throws NonUniqueResultException
     */
    protected <E> E getSingleResult(final TypedQuery<E> query) throws NonUniqueResultException {
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException(e.getMessage(), e);
        }
    }

    /**
     * Returns an entity by the specified id and throws the exception otherwise
     *
     * @param id the entity id
     * @return an account
     * @throws EntityNotFoundException if the entity doesn't exist
     */
    public T getOrThrow(final PK id) throws EntityNotFoundException {
        return ofNullable(this.read(id)).orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    /**
     * Sets the deleted field of the entity by entity id.
     *
     * @param id the entity id
     * @param value the value
     * @throws EntityNotFoundException if the entity doesn't exist
     */
    public void setDeleted(final PK id, final boolean value) throws EntityNotFoundException {
        T t = ofNullable(this.get(id)).orElseThrow(() -> new EntityNotFoundException(id.toString()));
        t.setDeleted(value);
        this.update(t);
    }

    protected EntityGraph<T> getGraph(final String ... attributeName) {
        EntityGraph<T> graph = em.createEntityGraph(entityClass);
        graph.addAttributeNodes(attributeName);
        return graph;
    }

    protected Map<String, Object> getHints(final String ... attributeName) {
        Map<String, Object> hints = new HashMap<>();
        hints.put(HINT_LOAD_GRAPH, getGraph(attributeName));
        return hints;
    }

    protected <S extends BaseEntity, X extends BaseEntity> Predicate isNotDeleted(final From<S, X> entity) {
        return this.em.getCriteriaBuilder().isFalse(entity.get(FIELD_DELETED));
    }

    protected <S extends IdEntity<PK>, X extends IdEntity<PK>> Predicate equalId(final From<S, X> entity, final PK id) {
        return this.em.getCriteriaBuilder().equal(entity.get(FIELD_ID), id);
    }

    private T get(PK id) {
        return this.em.find(entityClass, id);
    }
}
