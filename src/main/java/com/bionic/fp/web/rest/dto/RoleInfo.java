package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ROLE;
import com.bionic.fp.domain.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds role info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleInfo {

    @JsonProperty(ROLE.ID)                  private Long id;
    @JsonProperty(ROLE.NAME)                private String name;
    @JsonProperty(ROLE.CAN_ASSIGN_ROLES)    private Boolean canAssignRoles;
    @JsonProperty(ROLE.CAN_CHANGE_SETTINGS) private Boolean canChangeSettings;
    @JsonProperty(ROLE.CAN_ADD_PHOTOS)      private Boolean canAddPhotos;
    @JsonProperty(ROLE.CAN_VIEW_PHOTOS)     private Boolean canViewPhotos;
    @JsonProperty(ROLE.CAN_ADD_COMMENTS)    private Boolean canAddComments;
    @JsonProperty(ROLE.CAN_VIEW_COMMENTS)   private Boolean canViewComments;

    public RoleInfo() {
    }

    public RoleInfo(final Long id, final String name,
                    final Boolean canAssignRoles, final Boolean canChangeSettings, final Boolean canAddPhotos,
                    final Boolean canViewPhotos, final Boolean canAddComments, final Boolean canViewComments) {
        this.id = id;
        this.name = name;
        this.canAssignRoles = canAssignRoles;
        this.canChangeSettings = canChangeSettings;
        this.canAddPhotos = canAddPhotos;
        this.canViewPhotos = canViewPhotos;
        this.canAddComments = canAddComments;
        this.canViewComments = canViewComments;
    }

    public RoleInfo(final Role role) {
        this(role.getId(), role.getRole(), role.isCanAssignRoles(), role.isCanChangeSettings(),
                role.isCanAddPhotos(), role.isCanViewPhotos(), role.isCanAddComments(), role.isCanViewComments());
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

    public Boolean getCanAssignRoles() {
        return canAssignRoles;
    }

    public void setCanAssignRoles(Boolean canAssignRoles) {
        this.canAssignRoles = canAssignRoles;
    }

    public Boolean getCanChangeSettings() {
        return canChangeSettings;
    }

    public void setCanChangeSettings(Boolean canChangeSettings) {
        this.canChangeSettings = canChangeSettings;
    }

    public Boolean getCanAddPhotos() {
        return canAddPhotos;
    }

    public void setCanAddPhotos(Boolean canAddPhotos) {
        this.canAddPhotos = canAddPhotos;
    }

    public Boolean getCanViewPhotos() {
        return canViewPhotos;
    }

    public void setCanViewPhotos(Boolean canViewPhotos) {
        this.canViewPhotos = canViewPhotos;
    }

    public Boolean getCanAddComments() {
        return canAddComments;
    }

    public void setCanAddComments(Boolean canAddComments) {
        this.canAddComments = canAddComments;
    }

    public Boolean getCanViewComments() {
        return canViewComments;
    }

    public void setCanViewComments(Boolean canViewComments) {
        this.canViewComments = canViewComments;
    }
}
