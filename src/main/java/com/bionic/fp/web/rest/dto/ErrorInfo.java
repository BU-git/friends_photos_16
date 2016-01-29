package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Holds error for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {

    private String error;

    public ErrorInfo() {
    }

    public ErrorInfo(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
