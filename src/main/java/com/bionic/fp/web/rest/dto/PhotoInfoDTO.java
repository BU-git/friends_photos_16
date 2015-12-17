package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Photo;
import com.bionic.fp.web.rest.RestConstants.PARAM;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by franky_str on 26.11.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoInfoDTO {


    private String name;
    private String url;

    @JsonProperty(PARAM.OWNER_ID)
    private Long ownerID;

    public PhotoInfoDTO() {
    }

    public PhotoInfoDTO(final Photo photo) {
        this.name = photo.getName();
        this.url = photo.getUrl();
        this.ownerID = photo.getOwner().getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

}
