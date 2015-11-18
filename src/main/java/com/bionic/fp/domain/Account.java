package com.bionic.fp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private String password;

    @Column(name = "user_name")
    private String userName;

    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "fb_id")
    private String fbId;

    @Column(name = "fb_profile_url")
    private String fbProfileUrl;

    @Column(name = "fb_token")
    private String fbToken;

    @Column(name = "vk_id")
    private String vkId;

    @Column(name = "vk_token")
    private String vkToken;

    @Column(name = "vk_profile_url")
    private String vkProfileUrl;

    private boolean guest;

    private boolean active = true;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountGroupConnection> groupConnections = new ArrayList<>();

    public Account() {}

    public Account(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getFacebookId() {
        return fbId;
    }

    public void setFacebookId(String facebookId) {
        this.fbId = facebookId;
    }

    public String getFacebookToken() {
        return fbToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.fbToken = facebookToken;
    }

    public String getFacebookProfileUrl() {
        return fbProfileUrl;
    }

    public void setFacebookProfileUrl(String facebookProfileUrl) {
        this.fbProfileUrl = facebookProfileUrl;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getVkToken() {
        return vkToken;
    }

    public void setVkToken(String vkToken) {
        this.vkToken = vkToken;
    }

    public String getVkProfileUrl() {
        return vkProfileUrl;
    }

    public void setVkProfileUrl(String vkProfileUrl) {
        this.vkProfileUrl = vkProfileUrl;
    }

    public List<AccountGroupConnection> getGroupConnections() {
        return groupConnections;
    }

    public void setGroupConnections(List<AccountGroupConnection> groupConnections) {
        this.groupConnections = groupConnections;
    }

    public void addGroupConnection(AccountGroupConnection groupConnection) {
        groupConnections.add(groupConnection);
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", fbID='" + fbId + '\'' +
                ", fbProfile='" + fbProfileUrl + '\'' +
                ", fbToken='" + fbToken + '\'' +
                ", guest=" + guest +
                ", password='" + password + '\'' +
                ", profileImageURL='" + profileImageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", vkID='" + vkId + '\'' +
                ", vkProfileURL='" + vkProfileUrl + '\'' +
                ", vkToken='" + vkToken + '\'' +
                '}';
    }
}
