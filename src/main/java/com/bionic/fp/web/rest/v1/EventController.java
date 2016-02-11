package com.bionic.fp.web.rest.v1;

import com.bionic.fp.Constants;
import com.bionic.fp.domain.*;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.logic.impl.EventTypeNotFoundException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.*;
import com.bionic.fp.web.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.Constants.RestConstants.PARAM.*;
import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * This is REST web-service that handles event-related requests
 *
 * @author Sergiy Gabriel
 */
@RestController
@RequestMapping(API+V1+EVENTS)
public class EventController {

    @Autowired private EventService eventService;
    @Autowired private AccountEventService accountEventService;
    @Autowired private EventTypeService eventTypeService;
    @Autowired private CommentService commentService;
    @Autowired private PhotoService photoService;
    @Autowired private MethodSecurityService methodSecurityService;


    //***************************************
    //                 @GET
    //***************************************


    /**
     * Returns an event
     *
     * @param event_id the event id
     * @param fields the fields that should return the request, separated by commas,
     *               the order of insertion is not important
     * @return an event
     * @throws NotFoundException if the event is not found
     */
    @RequestMapping(value = EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public EventInfo findEventById(@PathVariable(EVENT.ID) final Long event_id,
                                   @RequestParam(value = FIELDS, required = false) final String fields) {
        Event event = this.findEventOrThrow(event_id);
        return EventInfo.Transformer.transform(event, fields);
    }

    /**
     * Returns a list of events as a result of the search according to the specified parameters
     *
     * @param name the event name
     * @param description the event description
     * @param fields the fields that should return the request, separated by commas,
     *               the order of insertion is not important
     * @return a list of events
     */
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

    /**
     * Returns a list of event ids as a result of the search according to the specified parameters
     *
     * @param name the event name
     * @param description the event description
     * @return a list of event ids
     */
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

    /**
     * Returns a list of events of the account
     *
     * @param accountId the account id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getAccountEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEvents(accountId);
    }

    /**
     * Returns a list of events of the user
     * The user must be authenticated
     *
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEvents(userId);
    }

    /**
     * Returns a list of events owned by the account
     *
     * @param accountId the account id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNTS+ACCOUNT_ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getOwnerEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEvents(accountId, Constants.RoleConstants.OWNER);
    }

    /**
     * Returns a list of events owned by the user.
     * The user must be authenticated
     *
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNTS+SELF+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getOwnerEvents() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEvents(userId, Constants.RoleConstants.OWNER);
    }

    /**
     * Returns a list of events where the account has the specified role
     *
     * @param accountId the account id
     * @param roleId the role id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getAccountEvents(@PathVariable(ACCOUNT.ID) final Long accountId,
                                                  @PathVariable(ROLE.ID) final Long roleId) {
        return this.getEvents(accountId, roleId);
    }

    /**
     * Returns a list of events where the user has the specified role.
     * The user must be authenticated
     *
     * @param roleId the role id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNTS+SELF+ROLES+ROLE_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents(@PathVariable(ROLE.ID) final Long roleId) {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEvents(userId, roleId);
    }

    /**
     * Return a list of event ids of the account
     *
     * @param accountId the account id
     * @return a list of event ids
     */
    @RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getAccountEventIds(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEventIds(accountId);
    }

    /**
     * Return a list of event ids of the user.
     * The user must be authenticated
     *
     * @return a list of event ids
     */
    @RequestMapping(value = ID+ACCOUNTS+SELF, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEventIds(userId);
    }

    /**
     * Return a list of event ids owned by the account
     *
     * @param accountId - account ID
     * @return a list of event ids
     */
    @RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getOwnerEventIds(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEventIds(accountId, Constants.RoleConstants.OWNER);
    }

    /**
     * Return a list of event ids owned by the user.
     * The user must be authenticated
     *
     * @return a list of event ids
     */
    @RequestMapping(value = ID+ACCOUNTS+SELF+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getOwnerEventIds() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEventIds(userId, Constants.RoleConstants.OWNER);
    }

    /**
     * Returns a list of event ids where the account has the specified role
     *
     * @param accountId the account id
     * @param roleId the role id
     * @return a list of event ids
     */
    @RequestMapping(value = ID+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getAccountEventIds(@PathVariable(ACCOUNT.ID) final Long accountId,
                                            @PathVariable(ROLE.ID) final Long roleId) {
        return this.getEventIds(accountId, roleId);
    }

    /**
     * Returns a list of event ids where the user has the specified role.
     * The user must be authenticated
     *
     * @param roleId the role id
     * @return a list of event ids
     */
    @RequestMapping(value = ID+ACCOUNTS+SELF+ROLES+ROLE_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds(@PathVariable(ROLE.ID) final Long roleId) {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEventIds(userId, roleId);
    }

    /**
     * Returns a list of events which are within the specified radius from the specified coordinate
     *
     * @param locationDto the dto which contains the center coordinate and the radius
     * @return a list of events
     */
    @RequestMapping(value = LOCATION+RADIUS, method = GET, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists findEventsInRadius(@RequestBody final LocationDto locationDto,
                                                    @RequestParam(value = FIELDS, required = false) final String fields) {
        EntityInfoLists body = new EntityInfoLists();
        body.setEvents(EventInfo.Transformer.transform(
                this.eventService.get(locationDto.getLocation(), locationDto.getRadius()), fields));
        return body;
    }

    /**
     * Returns a list of events in the specified coordinate range
     *
     * @param locationDto the dto which contains the South-West and North-East coordinates
     * @return a list of events
     */
    @RequestMapping(value = LOCATION+RANGE, method = GET, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists findEventsInRange(@RequestBody final LocationDto locationDto,
                                                   @RequestParam(value = FIELDS, required = false) final String fields) {
        EntityInfoLists body = new EntityInfoLists();
        body.setEvents(EventInfo.Transformer.transform(
                this.eventService.get(locationDto.getSw(), locationDto.getNe()), fields));
        return body;
    }


    //***************************************
    //                 @POST
    //***************************************


    /**
     * Creates an event and returns its id
     *
     * @param eventDto the event
     * @return the event id
     * @throws EventTypeNotFoundException if the event type is not found
     */
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
        if(eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if(eventDto.getGeo() != null) {
            event.setGeoServicesEnabled(eventDto.getGeo());
        }
        if(eventDto.getPrivate() != null) {
            event.setPrivate(eventDto.getPrivate());
            event.setPassword(eventDto.getPassword());
        }

        Long eventId = this.eventService.createEvent(userId, event);

        return new IdInfo(eventId);
    }

    /**
     * Adds the user to the event
     *
     * @param eventId the event id
     * @param password the event password (it will be required if the event is private)
     * @throws EventNotFoundException if the event is not found
     */
    @RequestMapping(value = EVENT_ID+ACCOUNTS, method = POST)
    @ResponseStatus(CREATED)
    public void addUserToEvent(@PathVariable(EVENT.ID) final Long eventId,
                               @RequestParam(value = EVENT.PASSWORD, required = false) final String password) {
        Long userId = this.methodSecurityService.getUserId();
        this.eventService.addOrUpdateAccountToEvent(userId, eventId, Constants.RoleConstants.MEMBER, password);
    }


    /**
     * Adds a comment to the event
     *
     * @param eventId the event id
     * @param commentDTO the comment
     * @throws EventNotFoundException if the event is not found
     */
    @RequestMapping(value = EVENT_ID+COMMENTS ,method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public IdInfo addComment(@PathVariable(value = EVENT.ID) final Long eventId,
                           @RequestBody final CommentDTO commentDTO) {
        methodSecurityService.checkPermission(eventId, Role::isCanAddComments);
        Comment comment = new Comment();
        comment.setAuthor(methodSecurityService.getUser());
        comment.setText(commentDTO.getCommentText());
        commentService.addCommentToEvent(eventId, comment);
        return new IdInfo(comment.getId()); // todo: check the id exists (not null)
    }

    /**
     * Add a photo to the event.
     * Save the photo file to filesystem and save photo info to DB
     *
     * @param eventId the event id
     * @param file the file
     * @param name the photo name
     * @param description the photo description
     * @return a photo info
     */
    @RequestMapping(value = EVENT_ID+PHOTOS, method = POST, consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ResponseBody
    public IdInfo addPhoto(@PathVariable(value = EVENT.ID) final Long eventId,
                           @RequestParam(PHOTO.FILE) final MultipartFile file,
                           @RequestParam(value = PHOTO.NAME, required = false) final String name,
                           @RequestParam(value = PHOTO.DESCRIPTION, required = false) final String description) throws IOException {
        Long userId = this.methodSecurityService.getUserId();
        Photo photo = this.photoService.saveToFileSystem(eventId, userId, file, name);
        return new IdInfo(photo.getId());
    }


    //***************************************
    //                 @PUT
    //***************************************


    /**
     * Updates the event
     *
     * @param eventId the event id
     * @param eventDto the event
     */
    @RequestMapping(value = EVENT_ID,  method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateEvent(@PathVariable(EVENT.ID) final Long eventId, @RequestBody final EventInput eventDto) {
        this.methodSecurityService.checkPermission(eventId, Role::isCanChangeSettings);

        Event event = this.getEventOrThrow(eventId); // todo: 400 => 404?

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
        if(eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if(eventDto.getGeo() != null) {
            event.setGeoServicesEnabled(eventDto.getGeo());
        }
        if(eventDto.getPrivate() != null) {
            event.setPrivate(eventDto.getPrivate());
        }
        // todo: checks logic
        if(eventDto.getPassword() != null) {
            event.setPassword(eventDto.getPassword());
        }

        this.eventService.update(event);
    }

    /**
     * Changes the role of the participant in the event
     *
     * @param eventId the event id
     * @param accountId the account id
     * @param roleId the new role id
     * @param password the event password (it will be required if the event is private)
     */
    @RequestMapping(value = EVENT_ID+ACCOUNTS+ACCOUNT_ID+ROLES, method = PUT)
    @ResponseStatus(OK)
    public void updateAccountToEvent(@PathVariable(EVENT.ID) final Long eventId,
                                     @PathVariable(ACCOUNT.ID) final Long accountId,
                                     @RequestParam(value = ROLE.ID, required = false) Long roleId,
                                     @RequestParam(value = EVENT.PASSWORD, required = false) final String password) {
        // todo: test and are this set of roles valid?
        this.methodSecurityService.checkPermission(eventId, Role::isCanChangeSettings, Role::isCanAssignRoles);
        this.eventService.addOrUpdateAccountToEvent(accountId, eventId, roleId, password);
    }


    //***************************************
    //                 @DELETE
    //***************************************


    /**
     * Deletes the event
     *
     * @param eventId the event id
     */
    @RequestMapping(value = EVENT_ID, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteEvent(@PathVariable(EVENT.ID) final Long eventId) {
        this.methodSecurityService.checkPermission(eventId, Role::isCanChangeSettings); // todo: change/add a new role to be responsible for this logic (maybe OWNER)
        this.eventService.softDelete(eventId);
    }

    /**
     * Deletes an account from the event
     *
     * @param eventId the event id
     */
    @RequestMapping(value = EVENT_ID+ACCOUNTS+ACCOUNT_ID, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteAccountFromEvent(@PathVariable(EVENT.ID) final Long eventId,
                                       @PathVariable(ACCOUNT.ID) final Long accountId) {
        this.methodSecurityService.checkPermission(eventId, Role::isCanAssignRoles); // todo: change/add a new role to be responsible for this logic
        this.accountEventService.delete(eventId, accountId);
    }

    /**
     * Deletes the user from the event
     *
     * @param eventId the event id
     */
    @RequestMapping(value = EVENT_ID+ACCOUNTS+SELF, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteUserFromEvent(@PathVariable(EVENT.ID) final Long eventId) {
        this.methodSecurityService.checkPermission(eventId, Role::isCanAssignRoles); // todo: change/add a new role to be responsible for this logic
        this.accountEventService.delete(eventId, this.methodSecurityService.getUserId());
    }


    //***************************************
    //                 PRIVATE
    //***************************************


    private EventType getEventTypeOrThrow(final Long eventTypeId) throws EventTypeNotFoundException {
        return ofNullable(this.eventTypeService.get(eventTypeId)).orElseThrow(() -> new EventTypeNotFoundException(eventTypeId));
    }

    private Event getEventOrThrow(final Long eventId) throws EventNotFoundException {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Event findEventOrThrow(final Long eventId) throws NotFoundException {
        return ofNullable(this.eventService.get(eventId)).orElseThrow(() -> new NotFoundException(eventId, "event"));
    }

    private EntityInfoLists getEvents(final Long accountId) throws InvalidParameterException {
        return this.getEvents(accountId, null);
    }

    private IdLists getEventIds(final Long accountId) throws InvalidParameterException {
        return this.getEventIds(accountId, null);
    }

    private EntityInfoLists getEvents(final Long accountId, final Long roleId) throws InvalidParameterException {
        List<Event> events = roleId == null ?
                this.accountEventService.getEvents(accountId) :
                this.accountEventService.getEvents(accountId, roleId);
        EntityInfoLists body = new EntityInfoLists();
        body.setEvents(events.stream().parallel()
                .map(EventInfo::new)
                .collect(Collectors.toList()));
        return body;
    }

    private IdLists getEventIds(final Long accountId, final Long roleId) throws InvalidParameterException {
        List<Long> events = roleId == null ?
                this.accountEventService.getEventIds(accountId) :
                this.accountEventService.getEventIds(accountId, roleId);
        IdLists body = new IdLists();
        body.setEvents(events);
        return body;
    }
}
