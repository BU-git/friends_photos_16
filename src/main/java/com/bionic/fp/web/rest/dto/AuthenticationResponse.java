package com.bionic.fp.web.rest.dto;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.Constants.RestConstants.PARAM;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds authentication response for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

    @JsonProperty(ACCOUNT.TOKEN) private String token;
    @JsonProperty(PARAM.USER_ID) private Long userId;
	@JsonProperty(PARAM.EMAIL) private String email;

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
