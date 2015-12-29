package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds user info for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class AccountInput {

    @JsonProperty(ACCOUNT.EMAIL)    private String email;
    @JsonProperty(ACCOUNT.PASSWORD) private String password;

    public AccountInput() {
    }

    public AccountInput(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}