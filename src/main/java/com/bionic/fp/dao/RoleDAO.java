package com.bionic.fp.dao;

import com.bionic.fp.domain.Role;

import java.util.List;

/**
 * Created by Yevhenii on 11/16/2015.
 */
public interface RoleDAO extends GenericDAO<Role, Integer> {

    Role getOwner();
    List<Role> getAllRoles();

//    Role getAdmin();

//    Role getAnonymous();

//    Role getGuest();

}
