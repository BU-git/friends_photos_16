package com.bionic.fp.rest;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.PermissionsDeniedException;
import com.bionic.fp.exception.app.logic.impl.EventNotFoundException;
import com.bionic.fp.exception.app.logic.impl.EventTypeNotFoundException;
import com.bionic.fp.exception.app.rest.NotFoundException;
import com.bionic.fp.rest.dto.EventCreateDTO;
import com.bionic.fp.rest.dto.EventInfoDTO;
import com.bionic.fp.rest.dto.EventUpdateDTO;
import com.bionic.fp.rest.dto.IdInfoDTO;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
import com.bionic.fp.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import static java.util.Optional.ofNullable;
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

        Long eventId = this.eventService.createEvent(eventDto.getOwnerId(), event);

        return new IdInfoDTO(eventId);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteEventById(@PathVariable("id") final Long eventId, HttpSession session) {
        Long ownerId = (Long) session.getAttribute("id");
        Role role = roleService.getRoleByAccountAndEvent(ownerId, eventId);
        if(role.isCanChangeSettings()) {
            this.eventService.remove(eventId);
        } else {
            throw new PermissionsDeniedException();
        }

    }

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody EventInfoDTO findEventById(@PathVariable("id") final Long id) {
        Event event = this.findEventOrThrow(id);
        return new EventInfoDTO(event);
    }

    @RequestMapping(value = "/{id:[\\d]+}",  method = PUT, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public void updateEvent(@PathVariable("id") final Long eventId, @RequestBody final EventUpdateDTO eventDto) {
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

        this.eventService.update(event);
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
}
