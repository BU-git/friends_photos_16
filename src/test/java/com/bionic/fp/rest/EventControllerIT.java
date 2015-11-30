package com.bionic.fp.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.rest.dto.EventCreateDTO;
import com.bionic.fp.rest.dto.EventUpdateDTO;
import org.junit.Test;

import java.util.List;

import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventController}
 *
 * @author Sergiy Gabriel
 */
public class EventControllerIT extends AbstractIT {

    private static final String EVENTS = "/events";
    private static final String EVENTS_ID = "/events/{id}";

    @Test
    public void testSaveEventSuccess() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        EventCreateDTO eventDto = new EventCreateDTO(getNewEventMax(), owner.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_CREATED);

        List<Event> events = this.accountService.getEvents(owner.getId());
        assertNotNull(events);
        assertEquals(1, events.size());
        Event actual = events.get(0);
        // or
//        Account account = this.accountService.getWithEvents(owner.getId());
//        assertNotNull(account);
//        Long accountEventId = account.getEvents().get(0).getId();
//        assertNotNull(accountEventId);
//        AccountEvent accountEvent = this.accountEventService.getWithAccountEvent(accountEventId);
//        assertNotNull(accountEvent);
//        Long eventId = accountEvent.getEvent().getId();
//        assertNotNull(eventId);
//        Event actual = this.eventService.get(eventId);
//        assertNotNull(actual);

        assertEquals(actual.getName(), eventDto.getName());
        assertEquals(actual.getDescription(), eventDto.getDescription());
        assertEquals(actual.getEventType(), privateEvent);
        assertEquals(actual.getLatitude(), eventDto.getLatitude());
        assertEquals(actual.getLongitude(), eventDto.getLongitude());
        assertEquals(actual.getRadius(), eventDto.getRadius());
        // by default
        assertEquals(actual.isVisible(), true);
        assertEquals(actual.isGeoServicesEnabled(), false);
        assertEquals(actual.getOwner().getId(), owner.getId());
        assertEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertEquals(actual.getOwner().getPassword(), owner.getPassword());
    }

    @Test
    public void testSaveEventShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        EventCreateDTO eventDto = new EventCreateDTO(getNewEventMax(), owner.getId());

        // without name, description, type
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description, type
        eventDto.setName("NY 2016");
        eventDto.setDescription(null);
        eventDto.setTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, description
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, type
        eventDto.setName(null);
        eventDto.setDescription("Happy New Year!");
        eventDto.setTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name
        eventDto.setName(null);
        eventDto.setDescription("Happy New Year!");
        eventDto.setTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without type
        eventDto.setName("NY 2016");
        eventDto.setDescription("Happy New Year!");
        eventDto.setTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description
        eventDto.setName("NY 2016");
        eventDto.setDescription(null);
        eventDto.setTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testSaveEventWithoutValidOwnerIdShouldReturnBadRequest() {
        EventCreateDTO eventDto = new EventCreateDTO(getNewEventMin(), null);

        // without owner ID
        eventDto.setOwnerId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // with a non-existent ID
        eventDto.setOwnerId(Long.MAX_VALUE);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testRemoveEventByIdSuccess() {
        Event event = getSavedEventMin(getSavedAccount());

        when()
            .delete(EVENTS_ID, event.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void testRemoveEventByIdShouldReturnNotFound() {
        when()
            .delete(EVENTS_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testRemoveEventByIdShouldReturnBadRequest() {
         when()
                .delete(EVENTS + "/{id}", Long.MAX_VALUE)
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testFindEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        when()
            .get(EVENTS_ID, event.getId())
        .then()
            .statusCode(SC_OK)
            .body("id.toString()", is(event.getId().toString()))
            .body("name", is(event.getName()))
            .body("type_id.toString()", is(event.getEventType().getId().toString()))
            .body("description", is(event.getDescription()))
            // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
            // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            .body("lat", is(event.getLatitude()))
            .body("lng", is(event.getLongitude()))
            .body("radius", is(event.getRadius()))
            .body("geo", is(event.isGeoServicesEnabled()))
            .body("visible", is(event.isVisible()))
            .body("owner_id.toString()", is(event.getOwner().getId().toString()));

        event = getSavedEventMax(owner);

        when()
            .get(EVENTS_ID, event.getId())
        .then()
            .statusCode(SC_OK)
            .body("id.toString()", is(event.getId().toString()))
            .body("name", is(event.getName()))
            .body("type_id.toString()", is(event.getEventType().getId().toString()))
            .body("description", is(event.getDescription()))
            // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
            // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            // digits
//            .body("lat.toString()", is(event.getLatitude().toString()))
//            .body("lng.toString()", is(event.getLongitude().toString()))
//            .body("radius.toString()", is(event.getRadius().toString()))
            .body("geo", is(event.isGeoServicesEnabled()))
            .body("visible", is(event.isVisible()))
            .body("owner_id.toString()", is(event.getOwner().getId().toString()));
    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
        long id = Long.MAX_VALUE;

        when()
            .get(EVENTS_ID, id)
        .then()
            .statusCode(SC_NOT_FOUND);
//            .body("error", is((new EventNotFoundException(id)).getMessage()));
    }

    @Test
    public void testUpdateEventSuccess() {
        Event event = getSavedEventMin(getSavedAccount());


        EventUpdateDTO eventDto = new EventUpdateDTO(updateEvent(getNewEventMax()));

        assertNotEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        // todo: when there will be more types
//        assertNotEquals(event.getEventType().getId(), eventDto.getTypeId());
        assertNotEquals(event.getLatitude(), eventDto.getLatitude());
        assertNotEquals(event.getLongitude(), eventDto.getLongitude());
        assertNotEquals(event.getRadius(), eventDto.getRadius());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertNotEquals(event.isVisible(), eventDto.getVisible());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(EVENTS_ID, event.getId())
        .then()
            .statusCode(SC_OK);

        event = this.eventService.get(event.getId());

        assertEquals(event.getName(), eventDto.getName());
        assertEquals(event.getDescription(), eventDto.getDescription());
        assertEquals(event.getEventType().getId(), eventDto.getTypeId());
        assertEquals(event.getLatitude(), eventDto.getLatitude());
        assertEquals(event.getLongitude(), eventDto.getLongitude());
        assertEquals(event.getRadius(), eventDto.getRadius());
        assertEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertEquals(event.isVisible(), eventDto.getVisible());


        eventDto = new EventUpdateDTO();
        eventDto.setName("NY 2020");

        assertNotEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        // todo: when there will be more types
//        assertNotEquals(event.getEventType().getId(), eventDto.getTypeId());
        assertNotEquals(event.getLatitude(), eventDto.getLatitude());
        assertNotEquals(event.getLongitude(), eventDto.getLongitude());
        assertNotEquals(event.getRadius(), eventDto.getRadius());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertNotEquals(event.isVisible(), eventDto.getVisible());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(EVENTS_ID, event.getId())
        .then()
            .statusCode(SC_OK);

        event = this.eventService.get(event.getId());

        assertEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        assertNotEquals(event.getEventType().getId(), eventDto.getTypeId());
        assertNotEquals(event.getLatitude(), eventDto.getLatitude());
        assertNotEquals(event.getLongitude(), eventDto.getLongitude());
        assertNotEquals(event.getRadius(), eventDto.getRadius());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertNotEquals(event.isVisible(), eventDto.getVisible());
    }

    @Test
    public void testUpdateEventShouldReturnNotFound() {
        EventUpdateDTO eventDto = new EventUpdateDTO();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(EVENTS_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testUpdateEventShouldReturnBadRequest() {
        EventUpdateDTO eventDto = new EventUpdateDTO();
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(EVENTS_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }
}