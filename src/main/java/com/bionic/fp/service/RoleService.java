package com.bionic.fp.service;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.permission.UserDoesNotExistException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Yevhenii Semenov on 11/17/2015.
 */
@Named
@Transactional
public class RoleService {

    private static final Integer OWNER_ROLE = 1;

    @Inject
    private RoleDAO roleDAO;

    @Inject
    private AccountEventDAO accountEventDAO;

    public RoleService() {}

    public void setRole(Account account, Event event, Role role) {
//        AccountEvent accountEvent = new AccountEvent();
//        accountEvent.setAccount(account);
//        accountEvent.setAccountId(account.getId());
//        accountEvent.setEvent(event);
//        accountEvent.setGroupId(event.getId());
//        accountEvent.setRoleId(role.getId());

    }

    public Role getOwner() {
        return this.roleDAO.getOwner();
    }

    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    public boolean setNewRole(Integer newRoleId, Long userId, Long eventId, Long ownerId) {
        AccountEvent accountEvent
                = accountEventDAO.getWithAccountEvent(userId, eventId);

        if (accountEvent == null) {
            return false;
        }

        // Can't downgrade owner
        if(accountEvent.getRole().getId().equals(OWNER_ROLE)) {
            return false;
        }

        // Can't make more than one owner
        if(newRoleId.equals(OWNER_ROLE)) {
            return false;
        }

        if(accountEvent.getEvent().getOwner().getId().equals(ownerId)) {
            Role newRole = roleDAO.read(newRoleId);
            accountEvent.setRole(newRole);
            accountEventDAO.update(accountEvent);
            return true;
        }

        return false;
    }

    public Role getRoleByAccountAndEvent(Long accountId, Long eventId) throws UserDoesNotExistException {
        AccountEvent accountEvent = accountEventDAO.getWithAccountEvent(accountId, eventId);
        if (accountEvent == null) {
            throw new UserDoesNotExistException();
        }
        return accountEvent.getRole();
    }
}
