package com.bionic.fp.dao;

import com.bionic.fp.domain.Role;

import java.util.List;

/**
 * Created by Yevhenii on 11/16/2015.
 */
public interface RoleDAO extends GenericDAO<Role, Long> {

    /**
     * Returns a list of all roles
     *
     * @return a list of roles
     */
    List<Role> getAllRoles();
}
