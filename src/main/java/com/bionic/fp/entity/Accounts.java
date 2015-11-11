package com.bionic.fp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Entity
@Table(name = "accounts")
public class Accounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "email")
    private String email;

    @Column(name = "fb_id")
    private String fbID;

    @Column(name = "fb_profile_url")
    private String fbProfile;

    @Column(name = "fb_token")
    private String fbToken;

    @Column(name = "guest")
    private boolean guest;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageURL;

    @Column(name = "userName")
    private String userName;

    @Column(name = "vk_id")
    private String vkID;

    @Column(name = "vk_profile_url")
    private String vkProfileURL;

    @Column(name = "vk_token")
    private String vkToken;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "account_goup",
                joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    private List<Groups> groups;

    public Accounts(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getFbProfile() {
        return fbProfile;
    }

    public void setFbProfile(String fbProfile) {
        this.fbProfile = fbProfile;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVkID() {
        return vkID;
    }

    public void setVkID(String vkID) {
        this.vkID = vkID;
    }

    public String getVkProfileURL() {
        return vkProfileURL;
    }

    public void setVkProfileURL(String vkProfileURL) {
        this.vkProfileURL = vkProfileURL;
    }

    public String getVkToken() {
        return vkToken;
    }

    public void setVkToken(String vkToken) {
        this.vkToken = vkToken;
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "id=" + id +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", fbID='" + fbID + '\'' +
                ", fbProfile='" + fbProfile + '\'' +
                ", fbToken='" + fbToken + '\'' +
                ", guest=" + guest +
                ", password='" + password + '\'' +
                ", profileImageURL='" + profileImageURL + '\'' +
                ", userName='" + userName + '\'' +
                ", vkID='" + vkID + '\'' +
                ", vkProfileURL='" + vkProfileURL + '\'' +
                ", vkToken='" + vkToken + '\'' +
                '}';
    }
}
