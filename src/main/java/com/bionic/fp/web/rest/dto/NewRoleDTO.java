package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.bionic.fp.Constants.RestConstants.*;

/**
 * Created by Yevhenii on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewRoleDTO {

    @JsonProperty(ACCOUNT.ID)
    private Long accountId;
    @JsonProperty(EVENT.ID)
    private Long eventId;
    @JsonProperty(ROLE.ID)
    private Long roleId;

    public NewRoleDTO() {
    }


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
