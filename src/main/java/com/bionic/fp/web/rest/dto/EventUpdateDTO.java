package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Event;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bionic.fp.web.rest.RestConstants.*;

/**
 * Holds event for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class EventUpdateDTO {

    @JsonProperty(EVENT_NAME)           private String name;
    @JsonProperty(EVENT_DESCRIPTION)    private String description;
    @JsonProperty(EVENT_TYPE_ID)        private Integer typeId;
    @JsonProperty(EVENT_LATITUDE)       private Double latitude;
    @JsonProperty(EVENT_LONGITUDE)      private Double longitude;
    @JsonProperty(EVENT_RADIUS)         private Float radius;
    @JsonProperty(EVENT_GEO)            private Boolean geo;
    @JsonProperty(EVENT_VISIBLE)        private Boolean visible;
    @JsonProperty(EVENT_PRIVATE)        private Boolean isPrivate;
    @JsonProperty(EVENT_PASSWORD)       private String password;

    public EventUpdateDTO() {
    }

    public EventUpdateDTO(final Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.typeId = event.getEventType().getId();
        this.visible = event.isVisible();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.radius = event.getRadius();
        this.geo = event.isGeoServicesEnabled();
        this.isPrivate = event.isPrivate();
        this.password = event.getPassword();
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    public Boolean getGeo() {
        return geo;
    }

    public void setGeo(Boolean geo) {
        this.geo = geo;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
