package com.bionic.fp.domain;

import java.io.Serializable;

/**
 * Used this interface for objects that need to work with primary key
 *
 * @author Sergiy Gabriel
 */
public interface IdEntity<PK extends Serializable> {

    PK getId();

    void setId(PK id);
}
