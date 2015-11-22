package com.bionic.fp.rest;

import com.bionic.fp.domain.Event;
import com.bionic.fp.rest.dto.GroupCreateDTO;
import com.bionic.fp.rest.dto.GroupInfoDTO;
import com.bionic.fp.rest.dto.GroupUpdateDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

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
        Event event = new Event();
        // required parameters (should not be null)
        event.setName(groupDto.getName());
        event.setDescription(groupDto.getDescription());
//        event.setEventType(groupDto.getType());
        event.setVisible(groupDto.isVisible());
        // optional parameters (possible null)
        event.setLongitude(groupDto.getLongitude());
        event.setLatitude(groupDto.getLatitude());

        Long groupId = this.groupService.createGroup(groupDto.getOwnerId(), event);

        return groupId != null ? new ResponseEntity(OK) : new ResponseEntity(BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    public @ResponseBody ResponseEntity deleteGroupById(@PathVariable("id") final Long id) {
        this.groupService.removeById(id);
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<GroupInfoDTO> findGroupById(@PathVariable("id") final Long id) {
        Event event = this.groupService.getByIdWithOwner(id);
        if(event == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        GroupInfoDTO body = new GroupInfoDTO(event);
        return new ResponseEntity<>(body, OK);
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity updateGroup(@RequestBody final GroupUpdateDTO groupDto) {
        Event event = this.groupService.getByIdWithOwnerAndAccounts(groupDto.getId());
        if(event == null) {
            return new ResponseEntity(NOT_FOUND);
        }

        // required parameters (should not be null)
        if(groupDto.getName() != null) {
            event.setName(groupDto.getName());
        }
        if(groupDto.getDescription() != null) {
            event.setDescription(groupDto.getDescription());
        }
/*        if(groupDto.getType() != null) {
            event.setEventType(groupDto.getType());
        }*/
        if(groupDto.getVisible() != null) {
            event.setVisible(groupDto.getVisible());
        }

        // optional parameters (possible null, but their use depends on the geolocationServicesEnabled,
        // therefore these parameters will not be cleared)
        if(groupDto.getLongitude() != null) {
            event.setLongitude(groupDto.getLongitude());
        }
        if(groupDto.getLatitude() != null) {
            event.setLatitude(groupDto.getLatitude());
        }

        this.groupService.update(event);

        return new ResponseEntity(OK);
    }
}