package com.bionic.fp.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * This class is a metamodel class that represents the {@link Event} entity
 *
 * @author Sergiy Gabriel
 */
@StaticMetamodel(Event.class)
public class Event_ {
    public static volatile SingularAttribute<Event, Account> owner;
    public static volatile SingularAttribute<Event, EventType> eventType;
    public static volatile ListAttribute<Event, AccountEvent> accounts;
    public static volatile ListAttribute<Event, Photo> photos;
    public static volatile ListAttribute<Event, Comment> comments;
}
