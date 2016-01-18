package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds authentication response for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class AuthenticationResponse {

    @JsonProperty(ACCOUNT.TOKEN) private String token;

    public AuthenticationResponse() {
        super();
    }

    public AuthenticationResponse(final String token) {
        this.setToken(token);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
