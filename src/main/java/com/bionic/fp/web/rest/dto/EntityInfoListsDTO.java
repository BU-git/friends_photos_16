package com.bionic.fp.web.rest.dto;

import com.bionic.fp.web.rest.RestConstants.EVENT;
import com.bionic.fp.web.rest.RestConstants.PHOTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Holds list of entities for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityInfoListsDTO {

//    private List<AccountInfoDTO> accounts;
    @JsonProperty(EVENT.LIST)       private List<EventInfoDTO> events;
    @JsonProperty(PHOTO.LIST)       private List<PhotoInfoDTO> photos;
//    private List<CommentInfoDTO> comments;
//    private List<TypeInfoDTO> types;
//    private List<RoleInfoDTO> roles;

    public EntityInfoListsDTO() {
    }

    public List<EventInfoDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventInfoDTO> events) {
        this.events = events;
    }

    public List<PhotoInfoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoInfoDTO> photos) {
        this.photos = photos;
    }
}
