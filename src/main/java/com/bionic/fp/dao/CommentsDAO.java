package com.bionic.fp.dao;

import com.bionic.fp.entity.Accounts;
import com.bionic.fp.entity.Comments;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Repository
public class CommentsDAO implements GenericDAO<Comments, Long> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    public CommentsDAO() {}

    @Override
    public Long create(Comments newInstance) {
        entityManager.persist(newInstance);
        return newInstance.getId();
    }

    @Override
    public Comments read(Long id) {
        return entityManager.find(Comments.class, id);
    }

    @Override
    public Comments update(Comments transientObject) {
        entityManager.merge(transientObject);
        return transientObject;
    }

    @Override
    public void delete(Long persistentObjectID) {
        entityManager.remove(read(persistentObjectID));
    }
}
