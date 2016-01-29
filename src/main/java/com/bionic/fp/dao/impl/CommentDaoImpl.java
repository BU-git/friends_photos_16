package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public CommentDaoImpl() {}

    @Override
    public List<Comment> getCommentsByEvent(final Long eventId) {
        Event event = this.getSingleResult(this.em.createNamedQuery(Event.FIND_COMMENTS, Event.class)
                .setParameter("eventId", eventId));
        return event == null ? Collections.emptyList() : event.getComments();
    }

    @Override
    public List<Comment> getCommentsByPhoto(final Long photoId) {
        Photo photo = this.getSingleResult(this.em.createNamedQuery(Photo.FIND_COMMENTS, Photo.class)
                .setParameter("photoId", photoId));
        return photo == null ? Collections.emptyList() : photo.getComments();
    }

}
