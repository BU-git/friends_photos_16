package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * This is an implementation of {@link CommentDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class CommentDaoImpl extends GenericDaoJpaImpl<Comment, Long> implements CommentDAO {

    private static final String AUTHOR = "author";
    private static final String COMMENTS = "comments";

    public CommentDaoImpl() {}

    @Override
    public List<Comment> getCommentsByEvent(final Long eventId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Event> event = query.from(Event.class);
        Join<Event, Comment> comment = event.join(COMMENTS);
        return this.em.createQuery(query.select(comment).where(cb.and(
                equalId(event, eventId), isNotDeleted(event), isNotDeleted(comment)))).getResultList();
    }

    @Override
    public List<Comment> getCommentsByPhoto(final Long photoId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Photo> photo = query.from(Photo.class);
        Join<Photo, Comment> comment = photo.join(COMMENTS);
        return this.em.createQuery(query.select(comment).where(cb.and(
                equalId(photo, photoId), isNotDeleted(photo), isNotDeleted(comment)))).getResultList();
    }

    @Override
    public List<Comment> getCommentsByAuthor(final Long authorId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Comment> comment = query.from(Comment.class);
        Join<Comment, Account> author = comment.join(AUTHOR);
        return this.em.createQuery(query.where(cb.and(
                equalId(author, authorId), isNotDeleted(author), isNotDeleted(comment)))).getResultList();
    }

}
