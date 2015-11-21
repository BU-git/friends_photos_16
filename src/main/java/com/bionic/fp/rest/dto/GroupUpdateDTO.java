package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Group;
import com.bionic.fp.domain.GroupType;

/**
 * Holds group for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class GroupUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private GroupType type;
    private Boolean visible;
    private Double latitude;
    private Double longitude;

    public GroupUpdateDTO() {
    }

    public GroupUpdateDTO(final Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.type = group.getGroupType();
        this.latitude = group.getLatitude();
        this.longitude = group.getLongitude();
        this.visible = group.isVisible();
    }

    public GroupUpdateDTO(final Group group, final Long ownerId) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.type = group.getGroupType();
        this.latitude = group.getLatitude();
        this.longitude = group.getLongitude();
        this.visible = group.isVisible();
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

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
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
