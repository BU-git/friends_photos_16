package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;

/**
 * Holds group for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class GroupCreateDTO {
    private String name;
    private String description;
    private EventType type;
    private Long ownerId;
    private boolean visible = true;
    private Double latitude;
    private Double longitude;

    public GroupCreateDTO() {
    }

    public GroupCreateDTO(final Event event, final Long ownerId) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.type = event.getEventType();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.visible = event.isVisible();
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
