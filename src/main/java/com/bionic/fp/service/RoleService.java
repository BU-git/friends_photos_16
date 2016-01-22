package com.bionic.fp.service;

import com.bionic.fp.Constants;
import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import com.bionic.fp.exception.permission.PermissionsDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * Created by Yevhenii Semenov on 11/17/2015.
 */
@Service
@Transactional
public class RoleService {

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

    /**
     * todo: delete it or fixed, see {@link EventService} method addOrUpdateAccountToEvent()
     */
    public boolean setNewRole(Long newRoleId, Long userId, Long eventId, Long ownerId) {
        AccountEvent accountEvent
                = accountEventDAO.getWithAccountEvent(userId, eventId);

        if (accountEvent == null) {
            throw new AccountEventNotFoundException(userId, eventId);
        }

        // Can't downgrade owner
        if(accountEvent.getRole().getId().equals(Constants.RoleConstants.OWNER)) {
            throw new PermissionsDeniedException();
        }

        // Can't make more than one owner
        if(newRoleId.equals(Constants.RoleConstants.OWNER)) {
            throw new PermissionsDeniedException();
        }

        //todo: fixme
//        if(accountEvent.getEvent().getOwner().getId().equals(ownerId)) {
//            Role newRole = roleDAO.read(newRoleId);
//            accountEvent.setRole(newRole);
//            accountEventDAO.update(accountEvent);
//            return true;
//        }

        throw new PermissionsDeniedException();
    }

    /**
     * Returns the user's role in the event by id of the user and the events, respectively
     *
     * @param accountId the account id
     * @param eventId the event id
     * @return the user's role in the event
     * @throws InvalidParameterException if the account id or the event id are not initialized
     * @throws AccountEventNotFoundException if the relationship between the user and the event is not found
     */
    public Role getRole(final Long accountId, final Long eventId)
                                                    throws InvalidParameterException, AccountEventNotFoundException {
        check(accountId != null, "The account ID should not be null");
        check(eventId != null, "The event ID should not be null");
        AccountEvent accountEvent = ofNullable(accountEventDAO.get(accountId, eventId))
                .orElseThrow(() -> new AccountEventNotFoundException(accountId, eventId));
        return accountEvent.getRole();
    }

    public Role getRole(final Long roleId) throws InvalidParameterException {
        check(roleId != null, "The role ID should not be null");
        return roleDAO.read(roleId);
    }

    public Role getRoleOrThrow(final Long roleId) throws InvalidParameterException, RoleNotFoundException {
        return ofNullable(getRole(roleId)).orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}
