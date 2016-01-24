package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds authentication request for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class AuthenticationSocialRequest {

    @JsonProperty(value = ACCOUNT.TOKEN, required = true)       private String token;
    @JsonProperty(value = ACCOUNT.SOCIAL_ID, required = true)   private String socialId;
    @JsonProperty(value = ACCOUNT.EMAIL, required = true)       private String email;
    @JsonProperty(value = ACCOUNT.USERNAME, required = false)   private String username;
    @JsonProperty(value = ACCOUNT.LAST_NAME, required = false)  private String lastName;
    @JsonProperty(value = ACCOUNT.FIRST_NAME, required = false) private String firstName;
    @JsonProperty(value = ACCOUNT.IMAGE_URL, required = false)  private String image;

    public AuthenticationSocialRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
