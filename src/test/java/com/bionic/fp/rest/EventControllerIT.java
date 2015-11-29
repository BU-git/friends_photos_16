package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.rest.dto.EventCreateDTO;
import com.bionic.fp.rest.dto.EventUpdateDTO;
import com.bionic.fp.service.AccountEventService;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.EventService;
import com.bionic.fp.service.EventTypeService;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

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
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class EventControllerIT {

    private static final String EVENTS = "/events";
    private static final String EVENTS_ID = "/events/{id}";

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountEventService accountEventService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

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

        // todo: throw an exception inside and handle it
        when()
            .delete(EVENTS + "/{id}", "99999")
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
        when()
            .get(EVENTS_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
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

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(EVENTS_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    private Account getSavedAccount() {
        String s = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        Account account = new Account("yaya@gmail.com" + s, "Yaya" + s, "yaya" + s);
        Long accountId = this.accountService.addAccount(account);
        assertNotNull(accountId);
        return account;
    }

    private EventType getPrivateEventType() {
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        return privateEvent;
    }

    private Event getNewEventMin() {
        Event event = new Event();
        LocalDateTime now = LocalDateTime.now();
        event.setName("Nano is " + now.getNano());
        event.setDescription("Today is " + now);
        event.setEventType(getPrivateEventType());

        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    private Event getNewEventMax() {
        Random random = new Random();
        Event event = getNewEventMin();
        event.setVisible(true);
        event.setLatitude(random.nextDouble());
        event.setLongitude(random.nextDouble());
        event.setRadius(0.1f);
        event.setGeoServicesEnabled(false);

        assertFalse(event.isDeleted());

        return event;
    }

    private Event getSavedEventMin(final Account owner) {
        Event event = getNewEventMin();

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    private Event getSavedEventMax(final Account owner) {
        Event event = getNewEventMax();

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    private Event getSavedEventMaxReverse(final Account owner) {
        Event event = updateEvent(getNewEventMax());

        Long eventId = this.eventService.createEvent(owner.getId(), event);

        assertNotNull(eventId);
        assertFalse(event.isDeleted());

        return event;
    }

    private Event updateEvent(final Event event) {
        event.setName(event.getName() + "_up");
        event.setDescription(event.getDescription() + "_up");
        event.setLatitude(event.getLatitude() == null ? 0 : event.getLatitude() + 1);
        event.setLongitude(event.getLongitude() == null ? 0 : event.getLongitude() + 1);
        event.setRadius(event.getRadius() == null ? 0 : event.getRadius() + 1);
        event.setVisible(!event.isVisible());
        event.setGeoServicesEnabled(!event.isGeoServicesEnabled());

        return event;
    }
}