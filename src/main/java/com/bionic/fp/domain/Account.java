package com.bionic.fp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "accounts")
@NamedEntityGraph(name = "Account.events",
        attributeNodes = @NamedAttributeNode(value = "events", subgraph = "events"),
        subgraphs = @NamedSubgraph(name = "events", attributeNodes = @NamedAttributeNode("event")))
@NamedQueries({
        @NamedQuery(name= Account.GET_BY_EMAIL, query="SELECT a FROM Account a WHERE a.email=:email"),
        @NamedQuery(name= Account.GET_BY_FB_ID, query="SELECT a FROM Account a WHERE a.fbId=:fbId"),
        @NamedQuery(name= Account.GET_BY_VK_ID, query="SELECT a FROM Account a WHERE a.vkId=:vkId")
})
public class Account implements Serializable {

    @Transient public static final String GET_BY_EMAIL = "Account.getByEmail";
    @Transient public static final String GET_BY_FB_ID = "Account.getByFbId";
    @Transient public static final String GET_BY_VK_ID = "Account.getByVkId";

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

//    private boolean guest;
//
//	@Column(name = "active")
//    private boolean active = true;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AccountEvent> events;

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

//    public boolean isGuest() {
//        return guest;
//    }
//
//    public void setGuest(boolean guest) {
//        this.guest = guest;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }

    public List<AccountEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AccountEvent> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (userName != null ? !userName.equals(account.userName) : account.userName != null) return false;
        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (fbId != null ? !fbId.equals(account.fbId) : account.fbId != null) return false;
        return !(vkId != null ? !vkId.equals(account.vkId) : account.vkId != null);

    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
//                ", active=" + active +
                ", email='" + email + '\'' +
                ", fbID='" + fbId + '\'' +
                ", fbProfile='" + fbProfileUrl + '\'' +
                ", fbToken='" + fbToken + '\'' +
//                ", guest=" + guest +
                ", password='" + password + '\'' +
                ", profileImageURL='" + profileImageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", vkID='" + vkId + '\'' +
                ", vkProfileURL='" + vkProfileUrl + '\'' +
                ", vkToken='" + vkToken + '\'' +
                '}';
    }
}
