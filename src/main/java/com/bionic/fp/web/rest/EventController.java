package com.bionic.fp.web.rest;

import com.bionic.fp.Constants;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.web.rest.RestConstants.*;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.web.security.SessionUtils;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
import com.bionic.fp.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import static com.bionic.fp.web.rest.RestConstants.PARAM.FIELDS;
import static com.bionic.fp.web.rest.RestConstants.PATH.*;
import static com.bionic.fp.web.security.SessionUtils.checkPermission;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * This is REST web-service that handles group-related requests
 *
 * @author Sergiy Gabriel
 */
@RestController
@RequestMapping(EVENTS)
public class EventController {

    @Inject
    private EventService eventService;

    @Inject
    private EventTypeService eventTypeService;

    @Inject
    private RoleService roleService;


    //***************************************
    //                 @GET
    //***************************************


    @RequestMapping(value = EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody EventInfoDTO findEventById(@PathVariable(EVENT.ID) final Long id,
                                                    @RequestParam(value = FIELDS, required = false) final String fields) {
        Event event = this.findEventOrThrow(id);
        return EventInfoDTO.Transformer.transform(event, fields);
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody EntityInfoListsDTO findEvents(
            @RequestParam(value = EVENT.NAME, required = false)         final String name,
            @RequestParam(value = EVENT.DESCRIPTION, required = false)  final String description,
            @RequestParam(value = FIELDS, required = false)             final String fields) {
        EntityInfoListsDTO body = new EntityInfoListsDTO();
        body.setEvents(EventInfoDTO.Transformer.transform(this.eventService.get(name, description), fields));
        return body;
    }

    @RequestMapping(value = ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO findEventIds(
            @RequestParam(value = EVENT.NAME, required = false)         final String name,
            @RequestParam(value = EVENT.DESCRIPTION, required = false)  final String description) {
        IdListsDTO body = new IdListsDTO();
        body.setEvents(this.eventService.get(name, description).stream().parallel()
                .map(Event::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+ACCOUNTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO getAccounts(@PathVariable(EVENT.ID) final Long eventId) {
        IdListsDTO body = new IdListsDTO();
        body.setAccounts(this.eventService.getAccounts(eventId).stream().parallel()
                .map(Account::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody EntityInfoListsDTO getPhotos(@PathVariable(EVENT.ID) final Long eventId) {
        EntityInfoListsDTO body = new EntityInfoListsDTO();
        body.setPhotos(this.eventService.getPhotos(eventId).stream().parallel()
                .map(PhotoInfoDTO::new).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+PHOTOS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO getPhotoIds(@PathVariable(EVENT.ID) final Long eventId) {
        IdListsDTO body = new IdListsDTO();
        body.setPhotos(this.eventService.getPhotos(eventId).stream().parallel()
                .map(Photo::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+COMMENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody IdListsDTO getComments(@PathVariable(EVENT.ID) final Long eventId) {
        IdListsDTO body = new IdListsDTO();
        body.setComments(this.eventService.getComments(eventId).stream().parallel()
                .map(Comment::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody IdInfoDTO getEventOwner(@PathVariable(EVENT.ID) final Long eventId) {
        return new IdInfoDTO(this.eventService.getOwner(eventId).getId());
    }


    //***************************************
    //                 @POST
    //***************************************


    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public @ResponseBody IdInfoDTO createEvent(@RequestBody final EventCreateDTO eventDto, final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);

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

        Long eventId = this.eventService.createEvent(userId, event);

        return new IdInfoDTO(eventId);
    }

    @RequestMapping(value = EVENT_ID+ACCOUNTS, method = POST)
    @ResponseStatus(CREATED)
    public void addAccountToEvent(@PathVariable(EVENT.ID) final Long eventId,
                                  @RequestParam(value = EVENT.PASSWORD, required = false) final String password,
                                  final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        this.eventService.addOrUpdateAccountToEvent(userId, eventId, Constants.RoleConstants.MEMBER, password);
    }


    //***************************************
    //                 @PUT
    //***************************************


    @RequestMapping(value = EVENT_ID,  method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateEvent(@PathVariable(EVENT.ID) final Long eventId, @RequestBody final EventUpdateDTO eventDto,
                            final HttpSession session) {
        checkPermission(session, this.roleService, eventId, Role::isCanChangeSettings);

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

    @RequestMapping(value = EVENT_ID+ACCOUNTS+ACCOUNT_ID, method = PUT)
    @ResponseStatus(OK)
    public void updateAccountToEvent(@PathVariable(EVENT.ID) final Long eventId,
                                     @PathVariable(ACCOUNT.ID) final Long accountId,
                                     @RequestParam(value = ROLE.ID, required = false) Integer roleId,
                                     @RequestParam(value = EVENT.PASSWORD, required = false) final String password,
                                     final HttpSession session) {
        // todo: test and is this roles valid?
        checkPermission(session, this.roleService, eventId, Role::isCanChangeSettings, Role::isCanAssignRoles);
        this.eventService.addOrUpdateAccountToEvent(accountId, eventId, roleId, password);
    }


    //***************************************
    //                 @DELETE
    //***************************************


    @RequestMapping(value = EVENT_ID, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteEventById(@PathVariable(EVENT.ID) final Long eventId, final HttpSession session) {
        checkPermission(session, this.roleService, eventId, Role::isCanChangeSettings);
        this.eventService.remove(eventId);
    }


    //***************************************
    //                 PRIVATE
    //***************************************


    private EventType getEventTypeOrThrow(final Integer eventTypeId) {
        return ofNullable(this.eventTypeService.get(eventTypeId)).orElseThrow(() -> new EventTypeNotFoundException(eventTypeId));
    }

    private Event getEventOrThrow(final Long eventId) {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Event findEventOrThrow(final Long eventId) {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new NotFoundException(eventId));
    }
}
