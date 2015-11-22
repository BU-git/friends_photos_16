package com.bionic.fp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Entity
@Table(name = "accounts")
public class Account implements Serializable {

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

    @Column(name = "user_name")
    private String userName;

    @Column(name = "vk_id")
    private String vkID;

    @Column(name = "vk_profile_url")
    private String vkProfile;

    @Column(name = "vk_token")
    private String vkToken;

    @OneToMany(mappedBy = "accounts")
    private List<AccountsGroups> groups;

    public Account(){}

    public Account(boolean active, String fbID, String fbProfile, String fbToken, String userName) {
        this.active = active;
        this.fbID = fbID;
        this.fbProfile = fbProfile;
        this.fbToken = fbToken;
        this.userName = userName;
    }

    public Account(String vkID, String vkProfileURL, String vkToken, String userName) {
        this.vkID = vkID;
        this.vkProfile = vkProfileURL;
        this.vkToken = vkToken;
        this.userName = userName;
    }

    public Account(boolean active, String email, String userName, String password) {
        this.active = active;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

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

    public String getVkProfile() {
        return vkProfile;
    }

    public void setVkProfile(String vkProfile) {
        this.vkProfile = vkProfile;
    }

    public String getVkToken() {
        return vkToken;
    }

    public void setVkToken(String vkToken) {
        this.vkToken = vkToken;
    }

    public List<AccountsGroups> getGroups() {
        return groups;
    }

    public void setGroups(List<AccountsGroups> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Account{" +
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
                ", vkProfile='" + vkProfile + '\'' +
                ", vkToken='" + vkToken + '\'' +
                '}';
    }
}
