package com.bionic.fp.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class containing mapping to error section returned by facebook Graph API
 * Created by Alexandr on 16.11.2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FBError {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("subcode")
    private int subcode;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubcode() {
        return subcode;
    }

    public void setSubcode(int subcode) {
        this.subcode = subcode;
    }

    @Override
    public String toString() {
        return "FBError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", subcode=" + subcode +
                '}';
    }
}
