package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.domain.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds user info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    @JsonProperty(ACCOUNT.ID)           private Long id;
    @JsonProperty(ACCOUNT.EMAIL)        private String email;
    @JsonProperty(ACCOUNT.USERNAME)     private String username;
    @JsonProperty(ACCOUNT.IMAGE_URL)    private String imageUrl;

    public UserInfo() {
    }

    public UserInfo(Long id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public UserInfo(Account account) {
        this(account.getId(), account.getUserName(), account.getProfileImageUrl());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
