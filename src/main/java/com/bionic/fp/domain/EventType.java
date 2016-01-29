package com.bionic.fp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Yevhenii Semenov on 11/16/2015.
 */
@Entity
@Table(name = "event_types")
public class EventType extends BaseEntity implements IdEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "type_name")
    private String typeName;

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", typeName='" + typeName +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        if (id != null ? !id.equals(eventType.id) : eventType.id != null) return false;
        return !(typeName != null ? !typeName.equals(eventType.typeName) : eventType.typeName != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }
}
