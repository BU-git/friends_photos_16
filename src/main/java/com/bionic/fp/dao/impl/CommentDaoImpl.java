package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */
@Repository
public class CommentDaoImpl implements CommentDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public CommentDaoImpl() {}

    @Override
    public Long create(Comment newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Comment read(Long id) {
        return entityManager.find(Comment.class, id);
    }

    @Override
    public Comment update(Comment transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}