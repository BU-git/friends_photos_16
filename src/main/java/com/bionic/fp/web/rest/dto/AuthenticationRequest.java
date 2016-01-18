package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds authentication request for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class AuthenticationRequest {

    @JsonProperty(value = ACCOUNT.EMAIL, required = true)       private String email;
    @JsonProperty(value = ACCOUNT.PASSWORD, required = true)    private String password;
    @JsonProperty(ACCOUNT.USERNAME)                             private String username;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(final String email, final String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public AuthenticationRequest(final String email, final String password, final String username) {
        this(email, password);
        this.setUsername(username);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
