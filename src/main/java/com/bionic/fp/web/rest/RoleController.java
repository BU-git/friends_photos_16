package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Role;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.service.RoleService;
import com.bionic.fp.web.security.SessionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Yevhenii on 11/29/2015.
 */
@RestController
@RequestMapping(API+ROLES)
public class RoleController {

    @Inject
    private RoleService roleService;
    final static Logger LOG = Logger.getLogger(RoleController.class);


    //***************************************
    //                 @GET
    //***************************************


    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<AllRolesDTO> getAllRoles() {
        List<Role> allRoles = roleService.getAllRoles();
        AllRolesDTO allRolesDTO = new AllRolesDTO(allRoles);

        return new ResponseEntity<>(allRolesDTO, OK);
    }

    @RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<RoleIdDTO> getRoleByAccountAndEvent(
            @PathVariable(ACCOUNT.ID) final Long accountId,
            @PathVariable(EVENT.ID) final Long eventId) {
        Role roleByAccountAndEvent = roleService.getRole(accountId, eventId);
        RoleIdDTO roleId = new RoleIdDTO(roleByAccountAndEvent.getId());
        return new ResponseEntity<>(roleId, OK);
    }


    //***************************************
    //                 @PUT
    //***************************************


    /**
     * todo: delete it or fixed, see {@link EventController} method updateAccountToEvent()
     */
    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void setNewRole(@RequestBody final NewRoleDTO newRoleDTO, final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        boolean isNewRoleSetted = roleService.setNewRole(
                newRoleDTO.getRoleId(),
                newRoleDTO.getAccountId(),
                newRoleDTO.getEventId(),
                userId
        );
    }

}
