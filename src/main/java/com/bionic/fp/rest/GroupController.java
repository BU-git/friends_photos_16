package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Group;
import com.bionic.fp.rest.dto.GroupCreateDTO;
import com.bionic.fp.rest.dto.GroupInfoDTO;
import com.bionic.fp.rest.dto.GroupUpdateDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * This is REST web-service that handles group-related requests
 *
 * @author Sergiy Gabriel
 */
@RestController
@RequestMapping("/groups")
public class GroupController {

    @Inject
    private GroupService groupService;

    @Inject
    private AccountService accountService;

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity saveGroup(@RequestBody final GroupCreateDTO groupDto) {
        if(groupDto.getName() == null || groupDto.getDescription() == null ||
                groupDto.getType() == null || groupDto.getOwnerId() == null) {
            return new ResponseEntity(BAD_REQUEST);
        }
//        Account owner = this.accountService.getByIdWithGroups(groupDto.getOwnerId());
//        if(owner == null) {
//            return new ResponseEntity(BAD_REQUEST);
//        }
        Group group = new Group();
        // required parameters (should not be null)
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());
        group.setGroupType(groupDto.getType());
        group.setVisible(groupDto.isVisible());
        // optional parameters (possible null)
        group.setLongitude(groupDto.getLongitude());
        group.setLatitude(groupDto.getLatitude());

        Long groupId = this.groupService.createGroup(groupDto.getOwnerId(), group);

        return groupId != null ? new ResponseEntity(OK) : new ResponseEntity(BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    public @ResponseBody ResponseEntity deleteGroupById(@PathVariable("id") final Long id) {
        this.groupService.removeById(id);
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<GroupInfoDTO> findGroupById(@PathVariable("id") final Long id) {
        Group group = this.groupService.getByIdWithOwner(id);
        if(group == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        GroupInfoDTO body = new GroupInfoDTO(group);
        return new ResponseEntity<>(body, OK);
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity updateGroup(@RequestBody final GroupUpdateDTO groupDto) {
        Group group = this.groupService.getByIdWithOwnerAndAccounts(groupDto.getId());
        if(group == null) {
            return new ResponseEntity(NOT_FOUND);
        }

        // required parameters (should not be null)
        if(groupDto.getName() != null) {
            group.setName(groupDto.getName());
        }
        if(groupDto.getDescription() != null) {
            group.setDescription(groupDto.getDescription());
        }
        if(groupDto.getType() != null) {
            group.setGroupType(groupDto.getType());
        }
        if(groupDto.getVisible() != null) {
            group.setVisible(groupDto.getVisible());
        }

        // optional parameters (possible null, but their use depends on the geolocationServicesEnabled,
        // therefore these parameters will not be cleared)
        if(groupDto.getLongitude() != null) {
            group.setLongitude(groupDto.getLongitude());
        }
        if(groupDto.getLatitude() != null) {
            group.setLatitude(groupDto.getLatitude());
        }

        this.groupService.update(group);

        return new ResponseEntity(OK);
    }
}
