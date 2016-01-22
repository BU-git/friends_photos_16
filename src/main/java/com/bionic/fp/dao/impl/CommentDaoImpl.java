package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

/**
 * Created by boubdyk on 11.11.2015.
 */
@Repository
public class CommentDaoImpl implements CommentDAO {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager em;

    public CommentDaoImpl() {}

    @Override
    public Long create(final Comment comment) {
        comment.setCreated(LocalDateTime.now());
        em.persist(comment);
        return comment.getId();
    }

    @Override
    public Comment read(Long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public Comment update(final Comment comment) {
        comment.setModified(LocalDateTime.now());
        em.merge(comment);
        return comment;
    }

    @Override
    public void delete(Long persistentObjectID) {
        em.remove(read(persistentObjectID));
    }
}
