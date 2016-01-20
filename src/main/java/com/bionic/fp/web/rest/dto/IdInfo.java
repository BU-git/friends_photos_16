package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Holds id for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdInfo {

    private Long id;

    public IdInfo() {
    }

    public IdInfo(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
