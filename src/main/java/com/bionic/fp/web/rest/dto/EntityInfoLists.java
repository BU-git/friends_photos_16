package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.bionic.fp.Constants.RestConstants.*;

import java.util.List;

/**
 * Holds list of entities for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityInfoLists {

    @JsonProperty(ACCOUNT.LIST)     private List<AccountInfo> accounts;
    @JsonProperty(EVENT.LIST)       private List<EventInfo> events;
    @JsonProperty(PHOTO.LIST)       private List<PhotoInfo> photos;
    @JsonProperty(COMMENT.LIST)     private List<CommentInfo> comments;
//    @JsonProperty(TYPE.LIST)        private List<TypeInfo> types;
//    @JsonProperty(ROLE.LIST)        private List<RoleInfo> roles;

    public EntityInfoLists() {
    }

    public List<AccountInfo> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountInfo> accounts) {
        this.accounts = accounts;
    }

    public List<EventInfo> getEvents() {
        return events;
    }

    public void setEvents(List<EventInfo> events) {
        this.events = events;
    }

    public List<PhotoInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoInfo> photos) {
        this.photos = photos;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
