package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.COMMENT;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.util.LocalDateTimeJsonDeserializer;
import com.bionic.fp.util.LocalDateTimeJsonSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * Holds comment info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentInfo {

    @JsonProperty(COMMENT.ID)           private Long id;
    @JsonProperty(COMMENT.TEXT)         private String text;
    @JsonProperty(COMMENT.AUTHOR_ID)    private Long authorId;
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonProperty(COMMENT.DATE)         private LocalDateTime date;

    public CommentInfo() {
    }

    public CommentInfo(final Long id, final String text, final LocalDateTime date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public CommentInfo(final Long id, final String text, final LocalDateTime date, final Long authorId) {
        this(id, text, date);
        this.authorId = authorId;
    }

    public CommentInfo(final Comment comment) {
        this(comment.getId(),comment.getText(), comment.getCreated(),
                (comment.getAuthor() != null && !comment.getAuthor().isDeleted()) ? comment.getAuthor().getId() : null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
