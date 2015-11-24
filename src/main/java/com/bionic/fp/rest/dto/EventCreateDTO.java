package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds group for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class EventCreateDTO {
    private String name;
    private String description;
    @JsonProperty("type_id")
    private Integer typeId;
    @JsonProperty("owner_id")
    private Long ownerId;
    private Boolean visible = true;
    private Double latitude;
    private Double longitude;
    private Float radius;
    private Boolean geolocation = false;

    public EventCreateDTO() {
    }

    public EventCreateDTO(final Event event, final Long ownerId) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.typeId = event.getEventType().getId();
        this.visible = event.isVisible();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.radius = event.getRadius();
        this.geolocation = event.isGeolocationServicesEnabled();
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        if(visible != null) {
            this.visible = visible;
        }
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Boolean getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Boolean geolocation) {
        if(geolocation != null) {
            this.geolocation = geolocation;
        }
    }
}