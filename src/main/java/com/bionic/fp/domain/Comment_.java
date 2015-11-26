package com.bionic.fp.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * This class is a metamodel class that represents the {@link Comment} entity
 *
 * @author Sergiy Gabriel
 */
@StaticMetamodel(Comment.class)
public class Comment_ {
    public static volatile SingularAttribute<Comment, Account> author;
}
