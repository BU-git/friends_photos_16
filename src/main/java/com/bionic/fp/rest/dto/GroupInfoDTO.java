package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Group;
import com.bionic.fp.domain.GroupType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Holds group info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class GroupInfoDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    @JsonProperty("expire_date")
    private LocalDateTime expireDate;
    private OwnerInfoDTO owner;
    private GroupType type;
    private Double latitude;
    private Double longitude;

    public GroupInfoDTO() {
    }

    public GroupInfoDTO(final Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.date = group.getDate();
        this.expireDate = group.getExpireDate();
        this.owner = new OwnerInfoDTO(group.getOwner());
        this.type = group.getGroupType();
        this.latitude = group.getLatitude();
        this.longitude = group.getLongitude();
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public OwnerInfoDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerInfoDTO owner) {
        this.owner = owner;
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

}
