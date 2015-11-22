package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;

/**
 * Holds group for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class GroupUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private EventType type;
    private Boolean visible;
    private Double latitude;
    private Double longitude;

    public GroupUpdateDTO() {
    }

    public GroupUpdateDTO(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.type = event.getEventType();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.visible = event.isVisible();
    }

    public GroupUpdateDTO(final Event event, final Long ownerId) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.type = event.getEventType();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.visible = event.isVisible();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
