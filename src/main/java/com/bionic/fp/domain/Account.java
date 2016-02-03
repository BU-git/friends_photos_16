package com.bionic.fp.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account extends BaseEntity implements IdEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(name = "user_name", nullable = true)
    private String userName;

    @Column(unique = true, nullable = false)
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

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<AccountEvent> events;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Photo> photos;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public Account() {}

    public Account(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public Account(final String email, final String username, final String password) {
        this(email, password);
        this.userName = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbProfileUrl() {
        return fbProfileUrl;
    }

    public void setFbProfileUrl(String fbProfileUrl) {
        this.fbProfileUrl = fbProfileUrl;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
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

    public List<AccountEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AccountEvent> events) {
        this.events = events;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (userName != null ? !userName.equals(account.userName) : account.userName != null) return false;
        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (profileImageUrl != null ? !profileImageUrl.equals(account.profileImageUrl) : account.profileImageUrl != null)
            return false;
        if (fbId != null ? !fbId.equals(account.fbId) : account.fbId != null) return false;
        if (fbProfileUrl != null ? !fbProfileUrl.equals(account.fbProfileUrl) : account.fbProfileUrl != null)
            return false;
        if (fbToken != null ? !fbToken.equals(account.fbToken) : account.fbToken != null) return false;
        if (vkId != null ? !vkId.equals(account.vkId) : account.vkId != null) return false;
        if (vkToken != null ? !vkToken.equals(account.vkToken) : account.vkToken != null) return false;
        return vkProfileUrl != null ? vkProfileUrl.equals(account.vkProfileUrl) : account.vkProfileUrl == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (profileImageUrl != null ? profileImageUrl.hashCode() : 0);
        result = 31 * result + (fbId != null ? fbId.hashCode() : 0);
        result = 31 * result + (fbProfileUrl != null ? fbProfileUrl.hashCode() : 0);
        result = 31 * result + (fbToken != null ? fbToken.hashCode() : 0);
        result = 31 * result + (vkId != null ? vkId.hashCode() : 0);
        result = 31 * result + (vkToken != null ? vkToken.hashCode() : 0);
        result = 31 * result + (vkProfileUrl != null ? vkProfileUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fbID='" + fbId + '\'' +
                ", fbProfile='" + fbProfileUrl + '\'' +
                ", fbToken='" + fbToken + '\'' +
                ", password='" + password + '\'' +
                ", profileImageURL='" + profileImageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", vkID='" + vkId + '\'' +
                ", vkProfileURL='" + vkProfileUrl + '\'' +
                ", vkToken='" + vkToken + '\'' +
                '}';
    }
}
