package com.bionic.fp.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * This class is a metamodel class that represents the {@link Account} entity
 *
 * @author Sergiy Gabriel
 */
@StaticMetamodel(Account.class)
public class Account_ {
    public static volatile ListAttribute<Account, AccountEvent> events;
}
