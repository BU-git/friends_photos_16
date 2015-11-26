package com.bionic.fp.rest.dto;

import com.bionic.fp.domain.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds owner info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OwnerInfoDTO {

    private Long id;
    @JsonProperty("user_name")
    private String userName;
    private String email;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    public OwnerInfoDTO() {
    }

    public OwnerInfoDTO(final Account account) {
        this.id = account.getId();
        this.userName = account.getUserName();
        this.email = account.getEmail();
        this.profileImageUrl = account.getProfileImageUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
