package com.bionic.fp.web.rest;

import com.bionic.fp.Constants;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.*;
import com.bionic.fp.web.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.bionic.fp.Constants.RestConstants.PARAM.*;
import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
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
@RequestMapping(API+EVENTS)
public class EventController {

    @Autowired private EventService eventService;
    @Autowired private EventTypeService eventTypeService;
    @Autowired private MethodSecurityService methodSecurityService;


    //***************************************
    //                 @GET
    //***************************************


    @RequestMapping(value = EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public EventInfo findEventById(@PathVariable(EVENT.ID) final Long id,
                                   @RequestParam(value = FIELDS, required = false) final String fields) {
        Event event = this.findEventOrThrow(id);
        return EventInfo.Transformer.transform(event, fields);
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public EntityInfoLists findEvents(@RequestParam(value = EVENT.NAME, required = false)        final String name,
                                      @RequestParam(value = EVENT.DESCRIPTION, required = false) final String description,
                                      @RequestParam(value = FIELDS, required = false)            final String fields) {
        EntityInfoLists body = new EntityInfoLists();
        body.setEvents(EventInfo.Transformer.transform(this.eventService.get(name, description), fields));
        return body;
    }

    @RequestMapping(value = ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdLists findEventIds(@RequestParam(value = EVENT.NAME, required = false)        final String name,
                                @RequestParam(value = EVENT.DESCRIPTION, required = false) final String description) {
        IdLists body = new IdLists();
        body.setEvents(this.eventService.get(name, description).stream().parallel()
                .map(Event::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+ACCOUNTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdLists getAccounts(@PathVariable(EVENT.ID) final Long eventId) {
        IdLists body = new IdLists();
        body.setAccounts(this.eventService.getAccounts(eventId).stream().parallel()
                .map(Account::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public EntityInfoLists getPhotos(@PathVariable(EVENT.ID) final Long eventId) {
        EntityInfoLists body = new EntityInfoLists();
        body.setPhotos(this.eventService.getPhotos(eventId).stream().parallel()
                .map(PhotoInfoDTO::new).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+PHOTOS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdLists getPhotoIds(@PathVariable(EVENT.ID) final Long eventId) {
        IdLists body = new IdLists();
        body.setPhotos(this.eventService.getPhotos(eventId).stream().parallel()
                .map(Photo::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+COMMENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdLists getComments(@PathVariable(EVENT.ID) final Long eventId) {
        IdLists body = new IdLists();
        body.setComments(this.eventService.getComments(eventId).stream().parallel()
                .map(Comment::getId).collect(toList()));
        return body;
    }

    @RequestMapping(value = EVENT_ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdInfo getEventOwner(@PathVariable(EVENT.ID) final Long eventId) {
        return new IdInfo(this.eventService.getOwner(eventId).getId());
    }


    //***************************************
    //                 @POST
    //***************************************


    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ResponseBody
    public IdInfo createEvent(@RequestBody final EventInput eventDto) {
        Long userId = this.methodSecurityService.getUserId();

        Event event = new Event();
        // required parameters (should not be null)
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        EventType eventType = this.getEventTypeOrThrow(eventDto.getEventTypeId());
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
            event.setPassword(eventDto.getPassword());
        }

        Long eventId = this.eventService.createEvent(userId, event);

        return new IdInfo(eventId);
    }

    @RequestMapping(value = EVENT_ID+ACCOUNTS, method = POST)
    @ResponseStatus(CREATED)
    public void addAccountToEvent(@PathVariable(EVENT.ID) final Long eventId,
                                  @RequestParam(value = EVENT.PASSWORD, required = false) final String password) {
        Long userId = this.methodSecurityService.getUserId();
        this.eventService.addOrUpdateAccountToEvent(userId, eventId, Constants.RoleConstants.MEMBER, password);
    }


    @RequestMapping(value = EVENT_ID+COMMENTS ,method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public void addComment(@PathVariable(value = EVENT.ID) final Long eventId,
                           @RequestBody final CommentDTO commentDTO) {
        methodSecurityService.checkPermission(eventId, Role::isCanAddComments);
        Event event = eventService.get(eventId);
        Comment comment = new Comment();
        comment.setAuthor(methodSecurityService.getUser());
        comment.setText(commentDTO.getCommentText());
        eventService.addComment(event, comment);
    }

    //***************************************
    //                 @PUT
    //***************************************


    @RequestMapping(value = EVENT_ID,  method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateEvent(@PathVariable(EVENT.ID) final Long eventId, @RequestBody final EventInput eventDto) {
        this.methodSecurityService.checkPermission(eventId, Role::isCanChangeSettings);

        Event event = this.getEventOrThrow(eventId);

        // required parameters (should not be null)
        if(eventDto.getEventTypeId() != null) {
            EventType eventType = this.getEventTypeOrThrow(eventDto.getEventTypeId());
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
        // todo: checks logic
        if(eventDto.getPassword() != null) {
            event.setPassword(eventDto.getPassword());
        }

        this.eventService.update(event);
    }

    @RequestMapping(value = EVENT_ID+ACCOUNTS+ACCOUNT_ID, method = PUT)
    @ResponseStatus(OK)
    public void updateAccountToEvent(@PathVariable(EVENT.ID) final Long eventId,
                                     @PathVariable(ACCOUNT.ID) final Long accountId,
                                     @RequestParam(value = ROLE.ID, required = false) Long roleId,
                                     @RequestParam(value = EVENT.PASSWORD, required = false) final String password) {
        // todo: test and is this roles valid?
        this.methodSecurityService.checkPermission(eventId, Role::isCanChangeSettings, Role::isCanAssignRoles);
        this.eventService.addOrUpdateAccountToEvent(accountId, eventId, roleId, password);
    }


    //***************************************
    //                 @DELETE
    //***************************************


    @RequestMapping(value = EVENT_ID, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteEventById(@PathVariable(EVENT.ID) final Long eventId) {
        this.methodSecurityService.checkPermission(eventId, Role::isCanChangeSettings);
        this.eventService.remove(eventId);
    }


    //***************************************
    //                 PRIVATE
    //***************************************


    private EventType getEventTypeOrThrow(final Long eventTypeId) {
        return ofNullable(this.eventTypeService.get(eventTypeId)).orElseThrow(() -> new EventTypeNotFoundException(eventTypeId));
    }

    private Event getEventOrThrow(final Long eventId) {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Event findEventOrThrow(final Long eventId) {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new NotFoundException(eventId));
    }
}
