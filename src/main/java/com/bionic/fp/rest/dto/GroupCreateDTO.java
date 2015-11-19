package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.GroupType;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class GroupCreateDTO {
    private String name;
    private String description;
    private GroupType type;
    private Long ownerId;

    public GroupCreateDTO() {
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
}
