package com.bionic.fp.dao;

import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;

import java.util.List;

/**
 * Created by Yevhenii on 11/16/2015.
 */
public interface RoleDAO extends GenericDAO<Role, Long> {

    Role getOwner() throws RoleNotFoundException;
    List<Role> getAllRoles();

//    Role getAdmin();

//    Role getAnonymous();

//    Role getGuest();

}
