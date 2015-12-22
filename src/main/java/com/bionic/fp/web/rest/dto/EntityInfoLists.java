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

//    private List<AccountInfoDTO> accounts;
    @JsonProperty(EVENT.LIST)       private List<EventInfo> events;
    @JsonProperty(PHOTO.LIST)       private List<PhotoInfoDTO> photos;
//    private List<CommentInfoDTO> comments;
//    private List<TypeInfoDTO> types;
//    private List<RoleInfoDTO> roles;

    public EntityInfoLists() {
    }

    public List<EventInfo> getEvents() {
        return events;
    }

    public void setEvents(List<EventInfo> events) {
        this.events = events;
    }

    public List<PhotoInfoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoInfoDTO> photos) {
        this.photos = photos;
    }
}
