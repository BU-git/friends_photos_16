package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by Yevhenii on 11/30/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllRolesDTO {

    List<Role> roles;

    public AllRolesDTO() {
    }

    public AllRolesDTO(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
