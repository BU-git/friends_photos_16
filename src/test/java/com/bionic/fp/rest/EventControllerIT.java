package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.rest.dto.EventCreateDTO;
import com.bionic.fp.rest.dto.EventUpdateDTO;
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

import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        EventCreateDTO eventDto = new EventCreateDTO();
        eventDto.setName("NY 2016");
        eventDto.setDescription("Happy New Year!");
        eventDto.setTypeId(privateEvent.getId());
        eventDto.setOwnerId(ownerId);
        eventDto.setVisible(true);
        eventDto.setLatitude(15.0);
        eventDto.setLongitude(25.0);
        eventDto.setRadius(0.5f);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENTS)
        .then()
            .statusCode(SC_CREATED);

        Account account = this.accountService.getByIdWithGroups(ownerId);
        assertNotNull(account);
        Long eventId = account.getEvents().get(0).getId();
        Event actual = this.eventService.getByIdWithOwner(eventId);
        assertNotNull(actual);

        assertEquals(actual.getId(), eventId);
        assertEquals(actual.getName(), eventDto.getName());
        assertEquals(actual.getDescription(), eventDto.getDescription());
        assertEquals(actual.getEventType(), privateEvent);
        assertEquals(actual.getLatitude(), eventDto.getLatitude());
        assertEquals(actual.getLongitude(), eventDto.getLongitude());
        assertEquals(actual.getRadius(), eventDto.getRadius());
        // by default
        assertEquals(actual.isVisible(), true);
        assertEquals(actual.isGeoServicesEnabled(), false);
        assertEquals(actual.getOwner().getId(), ownerId);
        assertEquals(actual.getOwner().getEmail(), owner.getEmail());
        assertEquals(actual.getOwner().getUserName(), owner.getUserName());
        assertEquals(actual.getOwner().getPassword(), owner.getPassword());
    }

    @Test
    public void testSaveEventShouldReturnBadRequest() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);

        EventCreateDTO eventDto = new EventCreateDTO();
        eventDto.setOwnerId(ownerId);
        eventDto.setVisible(true);
        eventDto.setLatitude(15.0);
        eventDto.setLongitude(25.0);
        eventDto.setRadius(0.5f);

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
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        EventCreateDTO eventDto = new EventCreateDTO();
        eventDto.setName("NY 400");
        eventDto.setDescription("Happy New Year!");
        eventDto.setTypeId(privateEvent.getId());

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
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2017");
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        when()
            .delete(EVENTS_ID, eventId)
        .then()
            .statusCode(SC_OK);
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
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("NY 2018");
        event.setDescription("Happy New Year!");
        event.setEventType(privateEvent);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        when()
            .get(EVENTS_ID, eventId)
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
    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
        when()
            .get(EVENTS_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testUpdateGroupSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        Long ownerId = this.accountService.addAccount(owner);
        assertNotNull(ownerId);
        EventType privateEvent = this.eventTypeService.getPrivate();
        assertNotNull(privateEvent);
        Event event = new Event();
        event.setName("-Failure-");
        event.setDescription("-Failure-");
        event.setEventType(privateEvent);
        Long eventId = this.eventService.createEvent(ownerId, event);
        assertNotNull(eventId);

        EventUpdateDTO eventDto = new EventUpdateDTO();
        eventDto.setName("NY 2019");
        eventDto.setDescription("Happy New Year!");
        eventDto.setTypeId(privateEvent.getId());
        eventDto.setVisible(false);
        eventDto.setLatitude(40.0);
        eventDto.setLongitude(40.0);
        eventDto.setRadius(0.1f);
        eventDto.setGeo(true);

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
            .put(EVENTS_ID, eventId)
        .then()
            .statusCode(SC_OK);

        event = this.eventService.getById(eventId);

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
            .put(EVENTS_ID, eventId)
        .then()
            .statusCode(SC_OK);

        event = this.eventService.getById(eventId);

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
    public void testUpdateGroupShouldReturnNotFound() {
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
}