package com.bionic.fp.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Yevhenii on 11/29/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleIdDTO {

    @JsonProperty("role_id")
    private Integer roleId;

    public RoleIdDTO(Integer roleId) {
        this.roleId = roleId;
    }

    public RoleIdDTO() {
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}