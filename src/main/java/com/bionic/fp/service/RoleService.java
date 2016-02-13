package com.bionic.fp.service;

import com.bionic.fp.dao.AccountEventDAO;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import com.bionic.fp.exception.logic.impl.RoleNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bionic.fp.Constants.RoleConstants.OWNER;
import static com.bionic.fp.util.Checks.check;
import static com.bionic.fp.util.Checks.checkNotNull;
import static com.bionic.fp.util.Checks.checkRole;
import static java.util.Optional.ofNullable;

/**
 * Created by Yevhenii Semenov on 11/17/2015.
 */
@Service
@Transactional
public class RoleService {

    @Autowired private RoleDAO roleDAO;
    @Autowired private AccountEventDAO accountEventDAO;

    public RoleService() {}

    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////


    /**
     * Returns a role by id and null otherwise
     *
     * @param roleId the role id
     * @return a role
     * @throws InvalidParameterException if incoming parameter is not valid
     */
    @Transactional(readOnly = true)
    public Role getRole(final Long roleId) throws InvalidParameterException {
        checkRole(roleId);
        return roleDAO.read(roleId);
    }

    /**
     * Saves the role and return it
     *
     * @param role the role
     * @return the role
     * @throws InvalidParameterException if incoming parameter is not valid
     */
    public Role create(final Role role) throws InvalidParameterException {
        checkRole(role);
        check(StringUtils.isNotEmpty(role.getRole()), "The role name is empty");
        return roleDAO.create(role);
    }

    /**
     * Updates the role and returns it or null if the role is not found
     *
     * @param role the role
     * @return the updated role
     * @throws InvalidParameterException incoming parameter is not valid
     */
    public Role update(final Role role) throws InvalidParameterException {
        checkRole(role);
        checkRole(role.getId());
        check(StringUtils.isNotEmpty(role.getRole()), "The role name is empty");
        Role actual = getRole(role.getId());
        if(actual != null && !actual.equals(role)) {
            return this.roleDAO.update(role);
        }
        return null;
    }


    //////////////////////////////////////////////
    //                  Other                   //
    //////////////////////////////////////////////


    /**
     * Returns the user's role in the event by id of the user and the events, respectively
     *
     * @param accountId the account id
     * @param eventId the event id
     * @return the user's role in the event
     * @throws InvalidParameterException if the account id or the event id are not initialized
     * @throws AccountEventNotFoundException if the relationship between the user and the event is not found
     */
    @Transactional(readOnly = true)
    public Role getRole(final Long accountId, final Long eventId)
            throws InvalidParameterException, AccountEventNotFoundException {
        checkNotNull(accountId, "account id");
        checkNotNull(eventId, "event id");
        AccountEvent accountEvent = ofNullable(accountEventDAO.get(accountId, eventId))
                .orElseThrow(() -> new AccountEventNotFoundException(accountId, eventId));
        return accountEvent.getRole();
    }

    @Transactional(readOnly = true)
    public Role getOwner() {
        return this.roleDAO.getOrThrow(OWNER);
    }

    /**
     * Returns a list of all roles
     *
     * @return a list of roles
     */
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    /**
     * Returns a role by id and throws the exception otherwise
     *
     * @param roleId the role id
     * @return a role
     * @throws InvalidParameterException if incoming parameter is not valid
     * @throws RoleNotFoundException if the role is not found
     */
    @Transactional(readOnly = true)
    public Role getRoleOrThrow(final Long roleId) throws InvalidParameterException, RoleNotFoundException {
        return ofNullable(getRole(roleId)).orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}
