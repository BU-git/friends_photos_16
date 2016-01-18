package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.Constants.RestConstants.PARAM;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds authentication response for the client-server communication
 *
 * @author Sergiy Gabriel
 */
public class AuthenticationResponse {

    @JsonProperty(ACCOUNT.TOKEN) private String token;
    @JsonProperty(PARAM.USER_ID) private Long userId;

    public AuthenticationResponse() {
        super();
    }

    public AuthenticationResponse(final String token) {
        this.setToken(token);
    }

    public AuthenticationResponse(final Long userId) {
        this.setUserId(userId);
    }

    public AuthenticationResponse(String token, Long userId) {
        this(token);
        this.setUserId(userId);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
