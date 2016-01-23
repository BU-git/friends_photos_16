package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.GenericDAO;
import com.bionic.fp.exception.logic.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Created by c2414 on 23.01.2016.
 */
public class GenericDaoJpaImpl<T, PK extends Serializable> implements GenericDAO<T, PK> {

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

    public GenericDaoJpaImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public PK create(T t) {
        this.em.persist(t);
        return t;
    }

    @Override
    public T read(PK id) {
        return null;
    }

    @Override
    public T update(T t) {
        return null;
    }

    @Override
    public void delete(PK id) throws EntityNotFoundException {

    }
}
