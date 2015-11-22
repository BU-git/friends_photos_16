package com.bionic.fp.service;

import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
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

    public void setRole(Account account, Event event, Role role) {
//        AccountEvent accountEvent = new AccountEvent();
//        accountEvent.setAccount(account);
//        accountEvent.setAccountId(account.getId());
//        accountEvent.setEvent(event);
//        accountEvent.setGroupId(event.getId());
//        accountEvent.setRoleId(role.getId());

    }
}
