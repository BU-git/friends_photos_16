package com.bionic.fp.service;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bionic.fp.util.Checks.checkNotNull;

/**
 * Created by Yevhenii on 1/4/2016.
 */

@Service
@Transactional
public class CommentService {

    @Autowired private CommentDAO commentDAO;

    public CommentService() {}

    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////

    public Long create(Comment comment) {
        commentDAO.create(comment);
        return comment.getId();
    }

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
    public List<Comment> getCommentsByEvent(final Long eventId) throws IncorrectPasswordException {
        checkNotNull(eventId, "event id");
        return commentDAO.getCommentsByEvent(eventId);
    }

    /**
     * Returns a list of the comments of the photo
     *
     * @param photoId the photo ID
     * @return a list of the comments of the photo
     * @throws IncorrectPasswordException if incoming parameter is not valid
     */
    public List<Comment> getCommentsByPhoto(final Long photoId) throws IncorrectPasswordException {
        checkNotNull(photoId, "photo id");
        return commentDAO.getCommentsByEvent(photoId);
    }


}
