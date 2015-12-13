package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Role;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.service.RoleService;
import com.bionic.fp.web.security.SessionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Yevhenii on 11/29/2015.
 */

@RestController
@RequestMapping("/role")
public class RoleController {

    @Inject
    private RoleService roleService;

    @RequestMapping(value = "/list", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<AllRolesDTO> getAllRoles() {
        List<Role> allRoles = roleService.getAllRoles();
        AllRolesDTO allRolesDTO = new AllRolesDTO(allRoles);

        return new ResponseEntity<>(allRolesDTO, OK);
    }

    @RequestMapping(value = "/change", method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void setNewRole(
            @RequestParam("account_id") final Long accountId,
            @RequestParam("event_id") final Long eventId,
            @RequestParam("role_id") final Integer roleId,
            final HttpSession session
    ) {
        Long userId = SessionUtils.getUserId(session);
        boolean isNewRoleSetted = roleService.setNewRole(
                roleId,
                accountId,
                eventId,
                userId
        );
    }

    @RequestMapping(value = "/account/{account_id:[\\d]+}/event/{event_id:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<RoleIdDTO> getRoleByAccountAndEvent(
            @PathVariable("account_id") final Long accountId,
            @PathVariable("event_id") final Long eventId
    ) {
        Role roleByAccountAndEvent = null;
        roleByAccountAndEvent = roleService.getRole(accountId, eventId);
        RoleIdDTO roleId = new RoleIdDTO(roleByAccountAndEvent.getId());
        return new ResponseEntity<>(roleId, OK);
    }




}
