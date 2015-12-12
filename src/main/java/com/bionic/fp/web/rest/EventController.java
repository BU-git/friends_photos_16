package com.bionic.fp.web.rest;

import com.bionic.fp.Constants;
import com.bionic.fp.dao.RoleDAO;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import com.bionic.fp.exception.permission.PermissionsDeniedException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.web.security.SessionUtils;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
import com.bionic.fp.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * This is REST web-service that handles group-related requests
 *
 * @author Sergiy Gabriel
 */
@RestController
@RequestMapping("/events")
public class EventController {

    @Inject
    private EventService eventService;

    @Inject
    private EventTypeService eventTypeService;

    @Inject
    private RoleService roleService;

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public @ResponseBody IdInfoDTO createEvent(@RequestBody final EventCreateDTO eventDto) {
        Event event = new Event();
        // required parameters (should not be null)
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        EventType eventType = this.getEventTypeOrThrow(eventDto.getTypeId());
        event.setEventType(eventType);

        // optional parameters (possible null)
        if(eventDto.getVisible() != null) {
            event.setVisible(eventDto.getVisible());
        }
        event.setLongitude(eventDto.getLongitude());
        event.setLatitude(eventDto.getLatitude());
        event.setRadius(eventDto.getRadius());
        if(eventDto.getGeo() != null) {
            event.setGeoServicesEnabled(eventDto.getGeo());
        }
        if(eventDto.getIsPrivate() != null) {
            event.setIsPrivate(eventDto.getIsPrivate());
        }
        event.setPassword(eventDto.getPassword());

        Long eventId = this.eventService.createEvent(eventDto.getOwnerId(), event);

        return new IdInfoDTO(eventId);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteEventById(@PathVariable("id") final Long eventId, final HttpSession session) {
        checkPermission(session, eventId, Role::isCanChangeSettings);
        this.eventService.remove(eventId);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody EventInfoDTO findEventById(@PathVariable("id") final Long id) {
        Event event = this.findEventOrThrow(id);
        return new EventInfoDTO(event);
    }

    @RequestMapping(value = "/{id:[\\d]+}",  method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateEvent(@PathVariable("id") final Long eventId, @RequestBody final EventUpdateDTO eventDto,
                            final HttpSession session) {
        checkPermission(session, eventId, Role::isCanChangeSettings);

        Event event = this.getEventOrThrow(eventId);

        // required parameters (should not be null)
        if(eventDto.getTypeId() != null) {
            EventType eventType = this.getEventTypeOrThrow(eventDto.getTypeId());
            event.setEventType(eventType);
        }
        if(eventDto.getName() != null) {
            event.setName(eventDto.getName());
        }
        if(eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }

        // optional parameters (possible null, but their use depends on the geoServicesEnabled,
        // therefore these parameters will not be cleared)
        if(eventDto.getVisible() != null) {
            event.setVisible(eventDto.getVisible());
        }
        if(eventDto.getLongitude() != null) {
            event.setLongitude(eventDto.getLongitude());
        }
        if(eventDto.getLatitude() != null) {
            event.setLatitude(eventDto.getLatitude());
        }
        if(eventDto.getRadius() != null) {
            event.setRadius(eventDto.getRadius());
        }
        if(eventDto.getGeo() != null) {
            event.setGeoServicesEnabled(eventDto.getGeo());
        }
        if(eventDto.getIsPrivate() != null) {
            event.setIsPrivate(eventDto.getIsPrivate());
        }
        if(eventDto.getPassword() != null) {
            event.setPassword(eventDto.getPassword());
        }

        this.eventService.update(event);
    }

    @RequestMapping(value = "/{event_id:[\\d]+}/account/{account_id:[\\d]+}", method = PUT)
    @ResponseStatus(OK)
    public void updateAccountToEvent(@PathVariable("event_id") final Long eventId,
                                     @PathVariable("account_id") final Long accountId,
                                     @RequestParam(value = "role_id", required = false) Integer roleId,
                                     @RequestParam(value = "password", required = false) final String password) {
        if(roleId == null) {
            roleId = Constants.RoleConstants.MEMBER;
        }
        this.eventService.addOrUpdateAccountToEvent(accountId, eventId, roleId, password);
    }

    @RequestMapping(value = "/{event_id:[\\d]+}/account/{account_id:[\\d]+}", method = POST)
    @ResponseStatus(OK)
    public void createAccountToEvent(@PathVariable("event_id") final Long eventId,
                                     @PathVariable("account_id") final Long accountId,
                                     @RequestParam(value = "role_id", required = false) Integer roleId,
                                     @RequestParam(value = "password", required = false) final String password) {
        if(roleId == null) {
            roleId = Constants.RoleConstants.MEMBER;
        }
        this.eventService.addOrUpdateAccountToEvent(accountId, eventId, roleId, password);
    }



    @RequestMapping(value = "/{id:[\\d]+}/accounts", method = GET)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO getAccounts(@PathVariable("id") final Long eventId) {
        IdListsDTO body = new IdListsDTO();
        body.setAccounts(this.eventService.getAccounts(eventId).stream().parallel()
                .map(Account::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = "/{id:[\\d]+}/photos", method = GET)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO getPhotos(@PathVariable("id") final Long eventId) {
        IdListsDTO body = new IdListsDTO();
        body.setPhotos(this.eventService.getPhotos(eventId).stream().parallel()
                .map(Photo::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = "/{id:[\\d]+}/comments", method = GET)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO getComments(@PathVariable("id") final Long eventId) {
        IdListsDTO body = new IdListsDTO();
        body.setComments(this.eventService.getComments(eventId).stream().parallel()
                .map(Comment::getId).collect(toList()));
        return body;
    }

    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO findEvent(@RequestParam(value = "name", required = false) final String name,
                                              @RequestParam(value = "description", required = false) final String description) {
        IdListsDTO body = new IdListsDTO();
        body.setEvents(this.eventService.get(name, description).stream().parallel()
                .map(Event::getId).collect(toList()));
        return body;
    }

    private EventType getEventTypeOrThrow(final Integer eventTypeId) {
        return ofNullable(this.eventTypeService.get(eventTypeId)).orElseThrow(() -> new EventTypeNotFoundException(eventTypeId));
    }

    private Event getEventOrThrow(final Long eventId) {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Event findEventOrThrow(final Long eventId) {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new NotFoundException(eventId));
    }

    private void checkPermission(final HttpSession session, final Long eventId, final Predicate<Role> ... predicates) {
        if(isNotEmpty(predicates)) {
            Long userId = SessionUtils.getUserId(session);
            try {
                Role role = roleService.getRole(userId, eventId);
                if(Stream.of(predicates).anyMatch(p -> p.negate().test(role))) {
                    throw new PermissionsDeniedException();
                }
            } catch (AccountEventNotFoundException e) {
                throw new PermissionsDeniedException();
            }
        }
    }
}
