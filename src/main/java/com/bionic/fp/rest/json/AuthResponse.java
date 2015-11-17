package com.bionic.fp.rest.json;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Class representing authentication response
 * Created by Schotkin Alexandr on 16.11.2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    public static final int AUTHENTICATED = 0;
    public static final int BAD_FB_TOKEN = 1;
    public static final int SERVER_PROBLEM = 2;

    private int code;
    private String userId;
    private String token;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(int code) {
        this.code = code;
    }

    public AuthResponse(int code, String message) {
        this(code);
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "code=" + code +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
