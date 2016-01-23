package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.GenericDAO;
import com.bionic.fp.domain.BaseEntity;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

/**
 * Created by c2414 on 23.01.2016.
 */
public class GenericDaoJpaImpl<T extends BaseEntity, PK extends Serializable> implements GenericDAO<T, PK> {

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
        return this.em.find(entityClass, id);
    }

    @Override
    public T update(T t) {
        t.setModified(LocalDateTime.now());
        this.em.merge(t);
        return t;
    }

    @Override
    public void delete(PK id) throws EntityNotFoundException {
        T t = this.getOrThrow(id);
        this.em.remove(t);
    }

    protected <T> T getSingleResult(final TypedQuery<T> query) throws NonUniqueResultException {
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException(e.getMessage(), e);
        }
    }

    public T getOrThrow(final PK id) throws EntityNotFoundException {
        return ofNullable(this.read(id)).orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public void setDeleted(final PK id, final boolean value) throws EntityNotFoundException {
        T t = this.getOrThrow(id);
        t.setDeleted(value);
        this.update(t);
    }
}
