package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Group;
import com.bionic.fp.domain.GroupType;

/**
 * Holds group for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class GroupCreateDTO {
    private String name;
    private String description;
    private GroupType type;
    private Long ownerId;
    private boolean visible;
    private Double latitude;
    private Double longitude;

    public GroupCreateDTO() {
    }

    public GroupCreateDTO(final Group group) {
        this.name = group.getName();
        this.description = group.getDescription();
        this.ownerId = group.getOwner().getId();
        this.type = group.getGroupType();
        this.latitude = group.getLatitude();
        this.longitude = group.getLongitude();
        this.visible = group.isVisible();
    }

    public GroupCreateDTO(final Group group, final Long ownerId) {
        this.name = group.getName();
        this.description = group.getDescription();
        this.ownerId = ownerId;
        this.type = group.getGroupType();
        this.latitude = group.getLatitude();
        this.longitude = group.getLongitude();
        this.visible = group.isVisible();
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

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
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
