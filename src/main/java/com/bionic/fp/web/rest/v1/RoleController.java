package com.bionic.fp.web.rest.v1;

import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PARAM.FIELDS;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.util.Checks.check;
import static com.bionic.fp.util.Checks.checkNotNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Yevhenii on 11/29/2015.
 */
@RestController
@RequestMapping(API+V1+ROLES)
public class RoleController {

    final static Logger LOG = Logger.getLogger(RoleController.class);

    @Autowired private RoleService roleService;
    @Autowired private MethodSecurityService methodSecurityService;


    //***************************************
    //                 @GET
    //***************************************

    /**
     * Returns a role
     *
     * @param roleId the role id
     * @return a role
     * @throws NotFoundException if the role is not found
     */
    @RequestMapping(value = ROLE_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public RoleInfo getRoleById(@PathVariable(ROLE.ID) final Long roleId) {
        Role role = ofNullable(this.roleService.getRole(roleId)).orElseThrow(() -> new NotFoundException(roleId));
        return new RoleInfo(role);
    }

    /**
     * Returns a list of all roles
     *
     * @return a list of roles
     */
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public EntityInfoLists getAllRoles() {
        EntityInfoLists body = new EntityInfoLists();
        body.setRoles(roleService.getAllRoles().stream().parallel()
                .map(RoleInfo::new).collect(toList()));
        return body;
    }

    /**
     * Returns a role of the account in the event
     *
     * @param accountId the account id
     * @param eventId the event id
     * @return a role
     */
    @RequestMapping(value = ACCOUNTS+ACCOUNT_ID+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public RoleInfo getRole(@PathVariable(ACCOUNT.ID) final Long accountId,
                            @PathVariable(EVENT.ID) final Long eventId) {
        Role role = findRoleOrThrow(accountId, eventId);
        return new RoleInfo(role);
    }

    /**
     * Returns a role id of the account in the event
     *
     * @param accountId the account id
     * @param eventId the event id
     * @return a role id
     */
    @RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdInfo getRoleId(@PathVariable(ACCOUNT.ID) final Long accountId,
                            @PathVariable(EVENT.ID) final Long eventId) {
        Role role = findRoleOrThrow(accountId, eventId);
        return new IdInfo(role.getId());
    }

    /**
     * Returns a role of the user in the event
     *
     * @param eventId the event id
     * @return a role
     */
    @RequestMapping(value = ACCOUNTS+SELF+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public RoleInfo getRole(@PathVariable(EVENT.ID) final Long eventId) {
        Role role = findRoleOrThrow(this.methodSecurityService.getUserId(), eventId);
        return new RoleInfo(role);
    }

    /**
     * Returns a role id of the user in the event
     *
     * @param eventId the event id
     * @return a role id
     */
    @RequestMapping(value = ID+ACCOUNTS+SELF+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdInfo getRoleId(@PathVariable(EVENT.ID) final Long eventId) {
        Role role = findRoleOrThrow(this.methodSecurityService.getUserId(), eventId);
        return new IdInfo(role.getId());
    }


    //***************************************
    //                 @POST
    //***************************************


    /**
     * Creates a role and returns its id
     *
     * @param input the role
     * @return the role id
     */
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ResponseBody
    public IdInfo createRole(@RequestBody final RoleInfo input) {
        checkNotNull(input.getCanAddComments(), "'can add comments'");
        checkNotNull(input.getCanAddPhotos(), "'can add photos'");
        checkNotNull(input.getCanAssignRoles(), "'can assign roles'");
        checkNotNull(input.getCanChangeSettings(), "'can change settings'");
        checkNotNull(input.getCanViewComments(), "'can view comments'");
        checkNotNull(input.getCanViewPhotos(), "'can view photos'");

        Role role = new Role();
        role.setRole(input.getName());
        role.setCanAddComments(input.getCanAddComments());
        role.setCanAddPhotos(input.getCanAddPhotos());
        role.setCanAssignRoles(input.getCanAssignRoles());
        role.setCanChangeSettings(input.getCanChangeSettings());
        role.setCanViewComments(input.getCanViewComments());
        role.setCanViewPhotos(input.getCanViewPhotos());

        role = this.roleService.create(role);

        return new IdInfo(role.getId());
    }


    //***************************************
    //                 @PUT
    //***************************************


    /**
     * Updates the role
     *
     * @param input the role
     */
    @RequestMapping(value = ROLE_ID,  method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateRole(@PathVariable(ROLE.ID) final Long roleId, @RequestBody final RoleInfo input) {

        Role role = ofNullable(this.roleService.getRole(roleId)).orElseThrow(() -> new NotFoundException(roleId));

        boolean modified = false;

        if(StringUtils.isNotEmpty(input.getName()) && !input.getName().equals(role.getRole())) {
            role.setRole(input.getName());
            modified = true;
        }
        if(input.getCanAddComments() != null && input.getCanAddComments() != role.isCanAddComments()) {
            role.setCanAddComments(input.getCanAddComments());
            modified = true;
        }
        if(input.getCanAddPhotos() != null && input.getCanAddPhotos() != role.isCanAddPhotos()) {
            role.setCanAddPhotos(input.getCanAddPhotos());
            modified = true;
        }
        if(input.getCanAssignRoles() != null && input.getCanAssignRoles() != role.isCanAssignRoles()) {
            role.setCanAssignRoles(input.getCanAssignRoles());
            modified = true;
        }
        if(input.getCanChangeSettings() != null && input.getCanChangeSettings() != role.isCanChangeSettings()) {
            role.setCanChangeSettings(input.getCanChangeSettings());
            modified = true;
        }
        if(input.getCanViewComments() != null && input.getCanViewComments() != role.isCanViewComments()) {
            role.setCanViewComments(input.getCanViewComments());
            modified = true;
        }
        if(input.getCanViewPhotos() != null && input.getCanViewPhotos() != role.isCanViewPhotos()) {
            role.setCanViewPhotos(input.getCanViewPhotos());
            modified = true;
        }

        if(modified) {
            this.roleService.update(role);
        }
    }


    //***************************************
    //                 @DELETE
    //***************************************


    /**
     * Deletes the role
     *
     * @param roleId the role id
     */
    @RequestMapping(value = ROLE_ID, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteEvent(@PathVariable(ROLE.ID) final Long roleId) {
//        this.roleService.softDelete(roleId);
    }


    //***************************************
    //                 PRIVATE
    //***************************************


    private Role findRoleOrThrow(final Long accountId, final Long eventId) {
        return ofNullable(roleService.getRole(accountId, eventId)).orElseThrow(() ->
                new NotFoundException(String.format("couldn't find a role by account '%d' and event '%d'.",
                        accountId, eventId)));
    }
}
