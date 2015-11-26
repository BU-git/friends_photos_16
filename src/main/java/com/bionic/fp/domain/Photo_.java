package com.bionic.fp.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * This class is a metamodel class that represents the {@link Photo} entity
 *
 * @author Sergiy Gabriel
 */
@StaticMetamodel(Photo.class)
public class Photo_ {
    public static volatile SingularAttribute<Photo, Event> event;
    public static volatile SingularAttribute<Photo, Account> owner;
    public static volatile ListAttribute<Photo, Comment> comments;
}
