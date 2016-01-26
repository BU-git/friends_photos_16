package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Holds list of entities id for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdLists {

    @JsonProperty(ACCOUNT.LIST) private List<Long> accounts;
    @JsonProperty(EVENT.LIST)   private List<Long> events;
    @JsonProperty(PHOTO.LIST)   private List<Long> photos;
    @JsonProperty(COMMENT.LIST) private List<Long> comments;
    @JsonProperty(TYPE.LIST)    private List<Long> types;
    @JsonProperty(ROLE.LIST)    private List<Long> roles;

    public IdLists() {
    }

    public List<Long> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Long> accounts) {
        this.accounts = accounts;
    }

    public List<Long> getEvents() {
        return events;
    }

    public void setEvents(List<Long> events) {
        this.events = events;
    }

    public List<Long> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Long> photos) {
        this.photos = photos;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public List<Long> getTypes() {
        return types;
    }

    public void setTypes(List<Long> types) {
        this.types = types;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }
}
