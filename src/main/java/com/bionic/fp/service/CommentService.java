package com.bionic.fp.service;

import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
