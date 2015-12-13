package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Yevhenii on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewRoleDTO {

    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("event_id")
    private Long eventId;
    @JsonProperty("role_id")
    private Integer roleId;

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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
