package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Simple JavaBean domain object which adds the creation date field and the date the entity was modified.
 * ALso contains a property to soft delete the domain object.
 * Used as a base class for objects needing this properties.
 *
 * @author Sergiy Gabriel
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

//    @Column(nullable = false) // doesn't work WTF?!?
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime created;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime modified;
    protected boolean deleted = false;

    protected BaseEntity() {
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(final LocalDateTime created) {
        this.created = created;
        // todo: why does it not work?
//        if(this.created == null) {
//            this.created = created;
//        }
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(final LocalDateTime modified) {
        this.modified = modified;
        // todo: why does it not work?
//        if(modified != null) {
//            if(this.modified != null) {
////                if(modified.isAfter(this.modified)) {
//                if(!modified.isBefore(this.modified)) {
//                    this.modified = modified;
//                }
//            } else {
//                if(modified.isAfter(this.created)) {
//                    this.modified = modified;
//                }
//            }
//        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
