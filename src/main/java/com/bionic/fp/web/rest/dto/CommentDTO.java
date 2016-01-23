package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Yevhenii on 1/5/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    @JsonProperty(Constants.RestConstants.COMMENT.TEXT)
    String commentText;

    public CommentDTO() {}

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
