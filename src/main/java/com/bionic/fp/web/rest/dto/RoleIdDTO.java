package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.bionic.fp.Constants.RestConstants.*;

/**
 * Created by Yevhenii on 11/29/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleIdDTO {

    @JsonProperty(ROLE.ID)
    private Long roleId;

    public RoleIdDTO(Long roleId) {
        this.roleId = roleId;
    }

    public RoleIdDTO() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
