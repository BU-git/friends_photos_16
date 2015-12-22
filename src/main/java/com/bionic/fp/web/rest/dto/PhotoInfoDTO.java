package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Photo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.bionic.fp.Constants.RestConstants.*;

/**
 * Created by franky_str on 26.11.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoInfoDTO {


    private String name;
    private String url;

    @JsonProperty(PARAM.OWNER_ID)
	private Long ownerId;
	@JsonProperty(EVENT.ID)
	private Long eventId;

    public PhotoInfoDTO() {
    }

    public PhotoInfoDTO(final Photo photo) {
        this.name = photo.getName();
        this.url = photo.getUrl();
        this.ownerId = photo.getOwner().getId();
		this.eventId = photo.getEvent().getId();
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}
