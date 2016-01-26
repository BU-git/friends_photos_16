package com.bionic.fp.dao;

import com.bionic.fp.domain.Comment;

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
}
