package com.bionic.fp.rest;

import com.bionic.fp.domain.Group;
import com.bionic.fp.rest.dto.GroupCreateDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.GroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * todo: comment
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
    public void saveGroup(@RequestBody final GroupCreateDTO groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());
        group.setGroupType(groupDto.getType());
        group.setOwner(this.accountService.getByIdWithGroups(groupDto.getOwnerId()));
        group.setLongitude(groupDto.getLongitude());
        group.setLatitude(groupDto.getLatitude());
        group.setDate(LocalDateTime.now());
        group.setVisible(groupDto.isVisible());

        this.groupService.addGroup(group);
    }
}
