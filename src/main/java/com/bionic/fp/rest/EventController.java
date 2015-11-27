package com.bionic.fp.rest;

import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.rest.dto.EventCreateDTO;
import com.bionic.fp.rest.dto.EventInfoDTO;
import com.bionic.fp.rest.dto.EventUpdateDTO;
import com.bionic.fp.rest.dto.IdInfoDTO;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

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

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<IdInfoDTO> createEvent(@RequestBody final EventCreateDTO eventDto) {
        Event event = new Event();
        // required parameters (should not be null)
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setEventType(this.eventTypeService.get(eventDto.getTypeId()));

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

        return eventId != null ? new ResponseEntity<>(new IdInfoDTO(eventId), CREATED) : new ResponseEntity<>(BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = DELETE)
    public @ResponseBody ResponseEntity deleteEventById(@PathVariable("id") final Long id) {
        return this.eventService.remove(id) ? new ResponseEntity(NO_CONTENT) : new ResponseEntity(BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[\\d]+}", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<EventInfoDTO> findEventById(@PathVariable("id") final Long id) {
        Event event = this.eventService.get(id);
        if(event == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        EventInfoDTO body = new EventInfoDTO(event);
        return new ResponseEntity<>(body, OK);
    }

    @RequestMapping(value = "/{id:[\\d]+}",  method = PUT, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity updateEvent(@PathVariable("id") final Long eventId, @RequestBody final EventUpdateDTO eventDto) {
        Event event = this.eventService.get(eventId);
        if(event == null) {
            return new ResponseEntity(NOT_FOUND);
        }

        // required parameters (should not be null)
        if(eventDto.getTypeId() != null) {
            EventType eventType = this.eventTypeService.get(eventDto.getTypeId());
            if(eventType == null) {
                return new ResponseEntity(BAD_REQUEST);
            }
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

        Event actual = this.eventService.update(event);

        return actual != null ? new ResponseEntity(OK) : new ResponseEntity(BAD_REQUEST);
    }
}
