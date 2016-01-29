package com.bionic.fp.domain;

import javax.persistence.*;

/**
 * Created by Yevhenii Semenov on 11/16/2015.
 */
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements IdEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @Column(name = "can_assign_roles")
    private boolean canAssignRoles;

    @Column(name = "can_change_settings")
    private boolean canChangeSettings;

    @Column(name = "can_view_photos")
    private boolean canViewPhotos;

    @Column(name = "can_add_comments")
    private boolean canAddComments;

    @Column(name = "can_view_comments")
    private boolean canViewComments;

    @Column(name = "can_add_photos")
    private boolean canAddPhotos;

    public Role() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCanAssignRoles() {
        return canAssignRoles;
    }

    public void setCanAssignRoles(boolean canAssignRoles) {
        this.canAssignRoles = canAssignRoles;
    }

    public boolean isCanChangeSettings() {
        return canChangeSettings;
    }

    public void setCanChangeSettings(boolean canChangeSettings) {
        this.canChangeSettings = canChangeSettings;
    }

    public boolean isCanViewPhotos() {
        return canViewPhotos;
    }

    public void setCanViewPhotos(boolean canViewPhotos) {
        this.canViewPhotos = canViewPhotos;
    }

    public boolean isCanAddComments() {
        return canAddComments;
    }

    public void setCanAddComments(boolean canAddComments) {
        this.canAddComments = canAddComments;
    }

    public boolean isCanViewComments() {
        return canViewComments;
    }

    public void setCanViewComments(boolean canViewComments) {
        this.canViewComments = canViewComments;
    }

    public boolean isCanAddPhotos() {
        return canAddPhotos;
    }

    public void setCanAddPhotos(boolean canAddPhotos) {
        this.canAddPhotos = canAddPhotos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role that = (Role) o;

        if (canAssignRoles != that.canAssignRoles) return false;
        if (canChangeSettings != that.canChangeSettings) return false;
        if (canViewPhotos != that.canViewPhotos) return false;
        if (canAddComments != that.canAddComments) return false;
        if (canViewComments != that.canViewComments) return false;
        if (canAddPhotos != that.canAddPhotos) return false;
        return role != null ? role.equals(that.role) : that.role == null;

    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (canAssignRoles ? 1 : 0);
        result = 31 * result + (canChangeSettings ? 1 : 0);
        result = 31 * result + (canViewPhotos ? 1 : 0);
        result = 31 * result + (canAddComments ? 1 : 0);
        result = 31 * result + (canViewComments ? 1 : 0);
        result = 31 * result + (canAddPhotos ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", canAssignRoles=" + canAssignRoles +
                ", canChangeSettings=" + canChangeSettings +
                ", canViewPhotos=" + canViewPhotos +
                ", canAddComments=" + canAddComments +
                ", canViewComments=" + canViewComments +
                ", canAddPhotos=" + canAddPhotos +
                '}';
    }
}
