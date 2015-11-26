package com.bionic.fp.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * This class is a metamodel class that represents the {@link AccountEvent} entity
 *
 * @author Sergiy Gabriel
 */
@StaticMetamodel(AccountEvent.class)
public class AccountEvent_ {
    public static volatile SingularAttribute<AccountEvent, Account> account;
    public static volatile SingularAttribute<AccountEvent, Event> event;
    public static volatile SingularAttribute<AccountEvent, Role> role;
}
