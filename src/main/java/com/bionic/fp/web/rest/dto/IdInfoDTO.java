package com.bionic.fp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Holds ID for the client-server communication
 *
 * @author Sergiy Gabriel
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdInfoDTO {

    private Long id;

    public IdInfoDTO() {
    }

    public IdInfoDTO(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
