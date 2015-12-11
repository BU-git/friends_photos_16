package com.bionic.fp.rest;

import com.bionic.fp.domain.Role;
import com.bionic.fp.rest.dto.*;
import com.bionic.fp.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Yevhenii on 11/29/2015.
 */

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Inject
    private RoleService roleService;

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<AllRolesDTO> getAllRoles() {
        List<Role> allRoles = roleService.getAllRoles();
        AllRolesDTO allRolesDTO = new AllRolesDTO(allRoles);

        return new ResponseEntity<>(allRolesDTO, OK);
    }

    @RequestMapping(value = "/change", method = PUT, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity setNewRole(@RequestBody final NewRoleDTO newRoleDTO) {
        boolean isNewRoleSetted = roleService.setNewRole(
                newRoleDTO.getRoleId(),
                newRoleDTO.getAccountId(),
                newRoleDTO.getEventId(),
                newRoleDTO.getOwnerId()
        );

        return isNewRoleSetted ? new ResponseEntity(OK) : new ResponseEntity(BAD_REQUEST);
    }

    @RequestMapping(value = "/for/{accountId:[\\d]+}/at/{eventId:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<RoleIdDTO> getRoleByAccountAndEvent(
            @PathVariable("accountId") final Long accountId,
            @PathVariable("eventId") final Long eventId
    ) {
        Role roleByAccountAndEvent = null;
        roleByAccountAndEvent = roleService.getRole(accountId, eventId);
        RoleIdDTO roleId = new RoleIdDTO(roleByAccountAndEvent.getId());
        return new ResponseEntity<>(roleId, OK);
    }




}
