package com.bionic.fp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Yevhenii Semenov on 11/16/2015.
 */
@Entity
@Table(name = "event_types")
public class EventType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Column(name = "type_name")
    private String typeName;

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", typeName='" + typeName +
                '}';
    }
}
