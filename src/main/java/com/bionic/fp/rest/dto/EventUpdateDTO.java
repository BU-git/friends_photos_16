package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Event;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds event for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class EventUpdateDTO {
    private String name;
    private String description;
    @JsonProperty("type_id")
    private Integer typeId;
    private Boolean visible;
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lng")
    private Double longitude;
    private Float radius;
    private Boolean geo;
    @JsonProperty("private")
    private Boolean isPrivate;
    private String password;

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
