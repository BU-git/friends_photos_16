package com.bionic.fp.dao;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link RoleDAO}
 *
 * @author Sergiy Gabriel
 */
public class RoleDaoIT extends AbstractDaoIT {

    @Test
    public void testGetPhotosByEventSuccess() {
        assertFalse(this.roleDAO.getAllRoles().isEmpty());
    }
}