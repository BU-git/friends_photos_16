package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * This is an implementation of {@link CommentDAO}
 *
 * @author Sergiy Gabriel
 */
@Repository
@Transactional
public class CommentDaoImpl extends GenericDaoJpaImpl<Comment, Long> implements CommentDAO {

    private static final String AUTHOR = "author";
    private static final String COMMENTS = "comments";

    public CommentDaoImpl() {}

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByEvent(final Long eventId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Event> event = query.from(Event.class);
        Join<Event, Comment> comment = event.join(COMMENTS);
        return this.em.createQuery(query.select(comment).where(cb.and(
                equalId(event, eventId), isNotDeleted(event), isNotDeleted(comment)))).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPhoto(final Long photoId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Photo> photo = query.from(Photo.class);
        Join<Photo, Comment> comment = photo.join(COMMENTS);
        return this.em.createQuery(query.select(comment).where(cb.and(
                equalId(photo, photoId), isNotDeleted(photo), isNotDeleted(comment)))).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByAuthor(final Long authorId) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Comment> comment = query.from(Comment.class);
        Join<Comment, Account> author = comment.join(AUTHOR);
        return this.em.createQuery(query.where(cb.and(
                equalId(author, authorId), isNotDeleted(author), isNotDeleted(comment)))).getResultList();
    }

    @Override
    public void delete(final Long id) {
        int rows = this.em.createNativeQuery("DELETE FROM photos_comments WHERE comment_id = ?")
                .setParameter(1, id).executeUpdate();
        if(rows == 0) {
            this.em.createNativeQuery("DELETE FROM events_comments WHERE comment_id = ?")
                    .setParameter(1, id).executeUpdate();
        }
        this.em.createNativeQuery("DELETE FROM comments WHERE id = ?")
                .setParameter(1, id).executeUpdate();
    }

    @Override
    public void createEventComment(final Long eventId, final Comment comment) {
        Comment actual = super.create(comment);
        super.em.createNativeQuery("INSERT INTO events_comments (event_id, comment_id) VALUES (?,?)")
                .setParameter(1, eventId)
                .setParameter(2, actual.getId())
                .executeUpdate();

    }

    @Override
    public void createPhotoComment(final Long photoId, final Comment comment) {
        Comment actual = super.create(comment);
        super.em.createNativeQuery("INSERT INTO photos_comments (photo_id, comment_id) VALUES (?,?)")
                .setParameter(1, photoId)
                .setParameter(2, actual.getId())
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Photo getPhotoOf(final Long commentId) {
        Query query = super.em.createNativeQuery("SELECT p.* FROM photos_comments pc LEFT JOIN photos p ON (pc.photo_id = p.id) WHERE comment_id = ?", Photo.class)
                .setParameter(1, commentId);
        return getSingleResult(query, Photo.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventOf(final Long commentId) {
        Query query = super.em.createNativeQuery("SELECT e.* FROM events_comments ec LEFT JOIN events e ON (ec.event_id = e.id) WHERE comment_id = ?", Event.class)
                .setParameter(1, commentId);
        return getSingleResult(query, Event.class);
    }

    private <T> T getSingleResult(Query query, Class<T> clz) {
        try {
            return clz.cast(query.getSingleResult());
        } catch (NoResultException ignored) {
            return null;
        }
    }
}
