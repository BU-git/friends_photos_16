package com.bionic.fp.service;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.dao.EventDAO;
import com.bionic.fp.dao.PhotoDAO;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.PhotoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.*;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Yevhenii on 1/4/2016.
 */
@Service
@Transactional
public class CommentService {

    @Autowired private CommentDAO commentDAO;
    @Autowired private EventDAO eventDAO;
    @Autowired private PhotoDAO photoDAO;

    public CommentService() {}

    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////

    public Long create(Comment comment) {
        commentDAO.create(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public Comment get(Long id) {
        return commentDAO.read(id);
    }

    public Comment update(Comment comment) {
        return commentDAO.update(comment);
    }

    public void softDelete(Long id) {
        commentDAO.setDeleted(id, true);
    }

    public void delete(Long id) {
        commentDAO.delete(id);
    }

    //////////////////////////////////////////////
    //                  Other                   //
    //////////////////////////////////////////////

    /**
     * Returns a list of the comments of the event
     *
     * @param eventId the event ID
     * @return a list of the comments of the event
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByEvent(final Long eventId) throws IncorrectPasswordException {
        checkEvent(eventId);
        return commentDAO.getCommentsByEvent(eventId);
    }

    /**
     * Returns a list of the comment ids of the event
     *
     * @param eventId the event ID
     * @return a list of the comment ids of the event
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public List<Long> getCommentIdsByEvent(final Long eventId) throws IncorrectPasswordException {
        return getCommentsByEvent(eventId).stream().parallel().map(Comment::getId).collect(toList());
    }

    /**
     * Returns a list of the comments of the photo
     *
     * @param photoId the photo ID
     * @return a list of the comments of the photo
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPhoto(final Long photoId) throws IncorrectPasswordException {
        checkPhoto(photoId);
        return commentDAO.getCommentsByPhoto(photoId);
    }

    /**
     * Returns a list of the comment ids of the photo
     *
     * @param photoId the photo ID
     * @return a list of the comment ids of the photo
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public List<Long> getCommentIdsByPhoto(final Long photoId) throws IncorrectPasswordException {
        return getCommentsByPhoto(photoId).stream().parallel().map(Comment::getId).collect(toList());
    }

    /**
     * Returns a list of the comments of the account
     *
     * @param accountId the account ID
     * @return a list of the comments of the account
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByAccount(final Long accountId) throws IncorrectPasswordException {
        checkAccount(accountId);
        return commentDAO.getCommentsByAuthor(accountId);
    }

    /**
     * Returns a list of the comment ids of the account
     *
     * @param accountId the account ID
     * @return a list of the comment ids of the account
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public List<Long> getCommentIdsByAccount(final Long accountId) throws IncorrectPasswordException {
        return getCommentsByAccount(accountId).stream().parallel().map(Comment::getId).collect(toList());
    }

    /**
     * Adds comment to event
     *
     * @param eventId the event id
     * @param comment the comment
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws EventNotFoundException if the event doesn't exist
     */
    public void addCommentToEvent(final Long eventId, final Comment comment) throws InvalidParameterException, EventNotFoundException {
        checkEvent(eventId);
        validation(comment);
        check(comment.getId() == null, "The comment is already exists");
//        Event event = this.eventDAO.getOrThrow(eventId);
//        Comment actual = this.commentDAO.create(comment);
//        event.getComments().add(actual);
        this.commentDAO.createEventComment(eventId, comment);
    }

    /**
     * Adds comment to photo
     *
     * @param photoId the photo id
     * @param comment the comment
     * @throws InvalidParameterException if incoming parameters are not valid
     * @throws PhotoNotFoundException if the photo doesn't exist
     */
    public void addCommentToPhoto(final Long photoId, final Comment comment) throws InvalidParameterException, PhotoNotFoundException {
        checkPhoto(photoId);
        validation(comment);
        check(comment.getId() == null, "The comment is already exists");
//        Photo photo = this.photoDAO.getOrThrow(photoId);
//        Comment actual = this.commentDAO.create(comment);
//        photo.getComments().add(actual);
        this.commentDAO.createPhotoComment(photoId, comment);
    }

    @Transactional(readOnly = true)
    public Photo getPhotoOf(final Long commentId) throws InvalidParameterException {
        checkComment(commentId);
        return this.commentDAO.getPhotoOf(commentId);
    }

    @Transactional(readOnly = true)
    public Event getEventOf(final Long commentId) throws InvalidParameterException {
        checkComment(commentId);
        return this.commentDAO.getEventOf(commentId);
    }

    //////////////////////////////////////////////
    //                 PRIVATE                  //
    //////////////////////////////////////////////

    private void validation(final Comment comment) throws InvalidParameterException {
        checkComment(comment);
        check(isNotEmpty(comment.getText()), "Comment is empty");
    }
}
