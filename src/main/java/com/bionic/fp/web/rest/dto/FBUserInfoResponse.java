package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * Created by Schotkin Alexandr on 07.11.2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FBUserInfoResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("id")
    private String id;

    @JsonProperty("error")
    private FBError error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FBError getError() {
        return error;
    }

    public void setError(FBError error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }

    @Override
    public String toString() {
        return "FBUserInfoResponse{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", error=" + error +
                '}';
    }
}
