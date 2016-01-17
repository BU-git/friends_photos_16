package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.web.rest.dto.EventInput;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.ACCOUNTS;
import static com.bionic.fp.Constants.RestConstants.PATH.EVENTS;
import static com.bionic.fp.Constants.RestConstants.PATH.API;
import static com.bionic.fp.Constants.RestConstants.PATH.EVENT_ID;
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
public class EventRestControllerIT extends AbstractIT {

    @Test
    public void testSaveEventSuccess() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        EventInput eventDto = new EventInput(getNewEventMax());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
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

        Account actualOwner = getEventOwner(actual.getId());
        assertEquals(actualOwner.getId(), owner.getId());
        assertEquals(actualOwner.getEmail(), owner.getEmail());
        assertEquals(actualOwner.getUserName(), owner.getUserName());
//        assertEquals(actualOwner.getPassword(), owner.getPassword());
    }

    @Test
    public void testSaveEventShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        EventInput eventDto = new EventInput(getNewEventMax());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        // without name, description, type
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description, type
        eventDto.setName("NY 2016");
        eventDto.setDescription(null);
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, description
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setEventTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, type
        eventDto.setName(null);
        eventDto.setDescription("Happy New Year!");
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name
        eventDto.setName(null);
        eventDto.setDescription("Happy New Year!");
        eventDto.setEventTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without type
        eventDto.setName("NY 2016");
        eventDto.setDescription("Happy New Year!");
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description
        eventDto.setName("NY 2016");
        eventDto.setDescription(null);
        eventDto.setEventTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testSaveEventWithoutValidOwnerIdShouldReturnUnautorized() {
        EventInput eventDto = new EventInput(getNewEventMin());

        // without owner ID

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
            .statusCode(SC_UNAUTHORIZED);

        // with a non-existent ID
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(Long.MAX_VALUE)).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+EVENTS)
        .then()
//            .statusCode(SC_UNAUTHORIZED); // todo: fixme
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testRemoveEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        when()
            .delete(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void testRemoveEventByIdShouldReturnUnauthorized() {
        Event event = getSavedEventMin(getSavedAccount());

        // no session

        when()
            .delete(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testRemoveEventByIdShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        // invalid session (user id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(Long.MAX_VALUE)).build();

        when()
            .delete(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // invalid session (event id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        when()
            .delete(API+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testRemoveEventByIdShouldReturnNotFound() {
        when()
            .delete(API+EVENTS + EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test @Ignore // todo: there is no necessary role
    public void testRemoveEventByIdShouldReturnBadRequest() {
         when()
            .delete(API+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testFindEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        when()
            .get(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK)
            .body(EVENT.ID + TO_STRING, is(event.getId().toString()))
            .body(EVENT.NAME, is(event.getName()))
            .body(EVENT.TYPE_ID + TO_STRING, is(event.getEventType().getId().toString()))
            .body(EVENT.DESCRIPTION, is(event.getDescription()))
            // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
            // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            .body(EVENT.LATITUDE, is(event.getLatitude()))
            .body(EVENT.LONGITUDE, is(event.getLongitude()))
            .body(EVENT.RADIUS, is(event.getRadius()))
            .body(EVENT.GEO, is(event.isGeoServicesEnabled()))
            .body(EVENT.VISIBLE, is(event.isVisible()));

        event = getSavedEventMax(owner);

        when()
            .get(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK)
            .body(EVENT.ID + TO_STRING, is(event.getId().toString()))
            .body(EVENT.NAME, is(event.getName()))
            .body(EVENT.TYPE_ID + TO_STRING, is(event.getEventType().getId().toString()))
            .body(EVENT.DESCRIPTION, is(event.getDescription()))
            // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
            // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            // digits
//            .body("lat.toString()", is(event.getLatitude().toString()))
//            .body("lng.toString()", is(event.getLongitude().toString()))
//            .body("radius.toString()", is(event.getRadius().toString()))
            .body(EVENT.GEO, is(event.isGeoServicesEnabled()))
            .body(EVENT.VISIBLE, is(event.isVisible()));

    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
        long id = Long.MAX_VALUE;

        when()
            .get(API+EVENTS + EVENT_ID, id)
        .then()
            .statusCode(SC_NOT_FOUND);
//            .body("error", is((new EventNotFoundException(id)).getMessage()));
    }

    @Test
    public void testUpdateEventSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        EventInput eventDto = new EventInput(updateEvent(getNewEventMax()));

        assertNotEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        // todo: when there will be more types
//        assertNotEquals(event.getEventType().getId(), eventDto.getTypeId());
        assertNotEquals(event.getLatitude(), eventDto.getLatitude());
        assertNotEquals(event.getLongitude(), eventDto.getLongitude());
        assertNotEquals(event.getRadius(), eventDto.getRadius());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertNotEquals(event.isVisible(), eventDto.getVisible());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK);

        event = this.eventService.get(event.getId());

        assertEquals(event.getName(), eventDto.getName());
        assertEquals(event.getDescription(), eventDto.getDescription());
        assertEquals(event.getEventType().getId(), eventDto.getEventTypeId());
        assertEquals(event.getLatitude(), eventDto.getLatitude());
        assertEquals(event.getLongitude(), eventDto.getLongitude());
        assertEquals(event.getRadius(), eventDto.getRadius());
        assertEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertEquals(event.isVisible(), eventDto.getVisible());


        eventDto = new EventInput();
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
            .put(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK);

        event = this.eventService.get(event.getId());

        assertEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        assertNotEquals(event.getEventType().getId(), eventDto.getEventTypeId());
        assertNotEquals(event.getLatitude(), eventDto.getLatitude());
        assertNotEquals(event.getLongitude(), eventDto.getLongitude());
        assertNotEquals(event.getRadius(), eventDto.getRadius());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
        assertNotEquals(event.isVisible(), eventDto.getVisible());
    }

    @Test
    public void testUpdateEventShouldReturnNotFound() {
        EventInput eventDto = new EventInput();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test @Ignore // todo: there is no necessary role
    public void testUpdateEventShouldReturnBadRequest() {
        EventInput eventDto = new EventInput();
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testUpdateEventShouldReturnUnauthorized() {
        Event event = getSavedEventMin(getSavedAccount());
        EventInput eventDto = new EventInput(updateEvent(getNewEventMax()));

        // no session

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testUpdateEventShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        EventInput eventDto = new EventInput(updateEvent(getNewEventMax()));

        // invalid session (user id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(Long.MAX_VALUE)).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // invalid session (event id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_FORBIDDEN);

        // no permission

        Account user = getSavedAccount();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user.getId()).getEvents().size());

        // todo: exchange "3"
        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), 3L, null);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user.getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testAddAccountToEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(getNewEventMax(), owner);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user1.getId())).build();

        given()
        .when()
            .post(API+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user2.getId())).build();

        given()
        .when()
            .post(API+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        Event newEvent = getSaved(getNewEventMax(), user2);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        given()
        .when()
            .post(API+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(2, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user1.getId())).build();

        given()
        .when()
            .post(API+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(3, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());
    }

    @Test
    public void testAddAccountToPrivateEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user1.getId())).build();

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
        when().
            post(API+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user2.getId())).build();

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
        when().
            post(API+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        Event newEvent = getSaved(setPrivate(getNewEventMax()), user2);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
        when().
            post(API+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(2, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user1.getId())).build();

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
        when().
            post(API+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId(), user1.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(3, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());
    }

    @Test
    public void testAddAccountToPrivateEventShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());

        // no password

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user1.getId())).build();

        given().
        when().
            post(API+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());

        // incorrect password

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user1.getId())).build();

        given().
            queryParam(EVENT.PASSWORD, event.getPassword() + "!").
        when().
            post(API+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
    }


}