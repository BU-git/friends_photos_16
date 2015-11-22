package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.dao.interfaces.RoleDAO;
import com.bionic.fp.entity.Account;
import com.bionic.fp.entity.AccountsGroups;
import com.bionic.fp.entity.Group;
import com.bionic.fp.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Yevhenii Semenov on 11/17/2015.
 */
@Named
@Transactional
public class RoleService {

    @Inject
    private RoleDAO roleDAO;

    public RoleService() {}

    public void setRole(Account account, Group group, Role role) {
        AccountsGroups accountsGroups = new AccountsGroups();
        accountsGroups.setAccount(account);
        accountsGroups.setAccountId(account.getId());
        accountsGroups.setGroup(group);
        accountsGroups.setGroupId(group.getId());
        accountsGroups.setRoleId(role.getId());

    }
}
