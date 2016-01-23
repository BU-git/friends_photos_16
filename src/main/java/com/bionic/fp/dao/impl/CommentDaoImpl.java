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
public class CommentDaoImpl extends GenericDaoJpaImpl<Comment, Long> implements CommentDAO {

    public CommentDaoImpl() {}

}
