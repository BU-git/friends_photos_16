package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.CommentDAO;
import com.bionic.fp.domain.Comment;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Yevhenii on 1/4/2016.
 */

@Named
@Transactional
public class CommentService {
    @Inject
    private CommentDAO commentDAO;

    public Long create(Comment comment) {
        return commentDAO.create(comment);
    }

    public Comment read(Long id) {
        return commentDAO.read(id);
    }

    public Comment update(Comment comment) {
        return commentDAO.update(comment);
    }

    public void delete(Long id) {
        commentDAO.delete(id);
    }


}
