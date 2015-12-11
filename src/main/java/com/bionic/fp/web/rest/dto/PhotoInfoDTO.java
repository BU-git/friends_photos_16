package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by franky_str on 26.11.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoInfoDTO {


    private String name;
    private String url;

    @JsonProperty("owner_id")
    private Long ownerID;

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

    public PhotoInfoDTO() {
        }

}
