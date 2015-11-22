package com.bionic.fp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Yevhenii Semenov on 11/16/2015.
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
