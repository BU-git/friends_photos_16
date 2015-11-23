package com.bionic.fp.dao;

import com.bionic.fp.domain.Role;

/**
 * Created by Yevhenii on 11/16/2015.
 */
public interface RoleDAO extends GenericDAO<Role, Integer> {

    Role getOwner();

//    Role getAdmin();

//    Role getAnonymous();

//    Role getGuest();

}
