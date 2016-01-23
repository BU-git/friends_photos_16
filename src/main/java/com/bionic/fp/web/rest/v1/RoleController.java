package com.bionic.fp.web.rest.v1;

import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.service.RoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Yevhenii on 11/29/2015.
 */
@RestController
@RequestMapping(API+REST_API_VERSION+ROLES)
public class RoleController {

    final static Logger LOG = Logger.getLogger(RoleController.class);

    @Autowired private RoleService roleService;
    @Autowired private MethodSecurityService methodSecurityService;


    //***************************************
    //                 @GET
    //***************************************

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


    //***************************************
    //                 @PUT
    //***************************************


    /**
     * todo: delete it or fixed, see {@link EventController} method updateAccountToEvent()
     */
    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void setNewRole(@RequestBody final NewRoleDTO newRoleDTO) {
        Long userId = this.methodSecurityService.getUserId();
        boolean isNewRoleSetted = roleService.setNewRole(
                newRoleDTO.getRoleId(),
                newRoleDTO.getAccountId(),
                newRoleDTO.getEventId(),
                userId
        );
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
