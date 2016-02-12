package com.bionic.fp.dao;

import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;

import java.util.List;

/**
 * Represents data access object of the comment
 *
 * @author Sergiy Gabriel
 */
public interface CommentDAO extends GenericDAO<Comment, Long> {

    /**
     * Returns a list of the comments of the event
     *
     * @param eventId the event ID
     * @return a list of the comments of the event
     */
    List<Comment> getCommentsByEvent(Long eventId);

    /**
     * Returns a list of the comments of the photo
     *
     * @param photoId the photo ID
     * @return a list of the comments of the photo
     */
    List<Comment> getCommentsByPhoto(Long photoId);

    /**
     * Returns a list of the comments of the author
     *
     * @param authorId the author ID
     * @return a list of the comments of the author
     */
    List<Comment> getCommentsByAuthor(Long authorId);

    /**
     * Creates the comment and adds it to the event
     *
     * @param eventId the event id
     * @param comment the comment
     */
    void createEventComment(Long eventId, Comment comment);

    /**
     * Creates the comment and adds it to the photo
     *
     * @param photoId the photo id
     * @param comment the comment
     */
    void createPhotoComment(Long photoId, Comment comment);

    /**
     * Returns a photo which has the comment and null otherwise
     *
     * @param commentId the comment id
     * @return a photo
     */
    Photo getPhotoOf(Long commentId);

    /**
     * Returns an event which has the comment and null otherwise
     *
     * @param commentId the comment id
     * @return an event
     */
    Event getEventOf(Long commentId);
}
