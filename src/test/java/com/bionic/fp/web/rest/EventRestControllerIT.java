package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.domain.Role;
import com.bionic.fp.web.rest.dto.EventCreateDTO;
import com.bionic.fp.web.rest.dto.EventUpdateDTO;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.bionic.fp.web.rest.RestConstants.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.lang.String.join;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventController}
 *
 * @author Sergiy Gabriel
 */
public class EventRestControllerIT extends AbstractIT {

    private static final String PATH_EVENT_ID = join("/", EVENT, "{eventId}");
    private static final String PATH_EVENT_ID_ACCOUNT_ID = join("/", PATH_EVENT_ID, ACCOUNT, "{accountId}");

    @Test
    public void testSaveEventSuccess() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        EventCreateDTO eventDto = new EventCreateDTO(getNewEventMax(), owner.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
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
            .post(EVENT)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // with a non-existent ID
        eventDto.setOwnerId(Long.MAX_VALUE);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(EVENT)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testRemoveEventByIdSuccess() {
        Event event = getSavedEventMin(getSavedAccount());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(event.getOwner().getId())).build();

        when()
            .delete(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void testRemoveEventByIdShouldReturnUnauthorized() {
        Event event = getSavedEventMin(getSavedAccount());

        // no session

        when()
            .delete(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testRemoveEventByIdShouldReturnForbidden() {
        Event event = getSavedEventMin(getSavedAccount());

        // invalid session (user id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(Long.MAX_VALUE)).build();

        when()
            .delete(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // invalid session (event id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(event.getOwner().getId())).build();

        when()
            .delete(PATH_EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testRemoveEventByIdShouldReturnNotFound() {
        when()
            .delete(PATH_EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test @Ignore // todo: there is no necessary role
    public void testRemoveEventByIdShouldReturnBadRequest() {
         when()
            .delete(EVENT + "/{id}", Long.MAX_VALUE)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testFindEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        when()
            .get(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK)
            .body(EVENT_ID + ".toString()", is(event.getId().toString()))
            .body(EVENT_NAME, is(event.getName()))
            .body(EVENT_TYPE_ID + ".toString()", is(event.getEventType().getId().toString()))
            .body(EVENT_DESCRIPTION, is(event.getDescription()))
            // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
            // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            .body(EVENT_LATITUDE, is(event.getLatitude()))
            .body(EVENT_LONGITUDE, is(event.getLongitude()))
            .body(EVENT_RADIUS, is(event.getRadius()))
            .body(EVENT_GEO, is(event.isGeoServicesEnabled()))
            .body(EVENT_VISIBLE, is(event.isVisible()))
            .body(OWNER_ID + ".toString()", is(event.getOwner().getId().toString()));

        event = getSavedEventMax(owner);

        when()
            .get(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK)
            .body(EVENT_ID + ".toString()", is(event.getId().toString()))
            .body(EVENT_NAME, is(event.getName()))
            .body(EVENT_TYPE_ID + ".toString()", is(event.getEventType().getId().toString()))
            .body(EVENT_DESCRIPTION, is(event.getDescription()))
            // sometimes failure 2015-11-24 16:51:53 == 2015-11-24 16:51:53.213
            // but 2015-11-24 16:51:53 != 2015-11-24 16:51:53.599
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            // digits
//            .body("lat.toString()", is(event.getLatitude().toString()))
//            .body("lng.toString()", is(event.getLongitude().toString()))
//            .body("radius.toString()", is(event.getRadius().toString()))
            .body(EVENT_GEO, is(event.isGeoServicesEnabled()))
            .body(EVENT_VISIBLE, is(event.isVisible()))
            .body(OWNER_ID + ".toString()", is(event.getOwner().getId().toString()));

    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
        long id = Long.MAX_VALUE;

        when()
            .get(PATH_EVENT_ID, id)
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

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(event.getOwner().getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(PATH_EVENT_ID, event.getId())
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
            .put(PATH_EVENT_ID, event.getId())
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
            .put(PATH_EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test @Ignore // todo: there is no necessary role
    public void testUpdateEventShouldReturnBadRequest() {
        EventUpdateDTO eventDto = new EventUpdateDTO();
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(PATH_EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testUpdateEventShouldReturnUnauthorized() {
        Event event = getSavedEventMin(getSavedAccount());
        EventUpdateDTO eventDto = new EventUpdateDTO(updateEvent(getNewEventMax()));

        // no session

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testUpdateEventShouldReturnForbidden() {
        Event event = getSavedEventMin(getSavedAccount());
        EventUpdateDTO eventDto = new EventUpdateDTO(updateEvent(getNewEventMax()));

        // invalid session (user id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(Long.MAX_VALUE)).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // invalid session (event id does not exist)

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(event.getOwner().getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(PATH_EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_FORBIDDEN);

        // no permission

        Account user = getSavedAccount();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(event.getOwner().getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user.getId()).getEvents().size());

        // todo: exchange "3"
        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), 3, null);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(event.getOwner().getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user.getId()).getEvents().size());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(user.getId())).build();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(PATH_EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testAddAccountToEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(getNewEventMax(), owner);
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        given()
            .queryParam(ROLE_ID, role.getId())
        .when()
            .post(PATH_EVENT_ID_ACCOUNT_ID, event.getId(), user1.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        given()
            .queryParam(ROLE_ID, role.getId())
        .when()
            .post(PATH_EVENT_ID_ACCOUNT_ID, event.getId(), user2.getId())
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

        given()
            .queryParam(ROLE_ID, role.getId())
        .when()
            .post(PATH_EVENT_ID_ACCOUNT_ID, newEvent.getId(), owner.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(2, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        given()
            .queryParam(ROLE_ID, role.getId())
        .when()
            .post(PATH_EVENT_ID_ACCOUNT_ID, newEvent.getId(), user1.getId())
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
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());
        given().
            queryParam(ROLE_ID, role.getId()).
            queryParam(EVENT_PASSWORD, event.getPassword()).
        when().
            put(PATH_EVENT_ID_ACCOUNT_ID, event.getId(), user1.getId()).
        then().
            statusCode(SC_OK);

        assertEquals(2, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        given().
            queryParam(ROLE_ID, role.getId()).
            queryParam(EVENT_PASSWORD, event.getPassword()).
        when().
            put(PATH_EVENT_ID_ACCOUNT_ID, event.getId(), user2.getId()).
        then().
            statusCode(SC_OK);

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

        given().
            queryParam(ROLE_ID, role.getId()).
            queryParam(EVENT_PASSWORD, event.getPassword()).
        when().
            put(PATH_EVENT_ID_ACCOUNT_ID, newEvent.getId(), owner.getId()).
        then().
            statusCode(SC_OK);

        assertEquals(3, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(2, this.eventService.getWithAccounts(newEvent.getId()).getAccounts().size());
        assertEquals(2, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(1, this.accountService.getWithEvents(user1.getId()).getEvents().size());
        assertEquals(2, this.accountService.getWithEvents(user2.getId()).getEvents().size());

        given().
            queryParam(ROLE_ID, role.getId()).
            queryParam(EVENT_PASSWORD, event.getPassword()).
        when().
            put(PATH_EVENT_ID_ACCOUNT_ID, newEvent.getId(), user1.getId()).
        then().
            statusCode(SC_OK);

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
        Role role = this.roleService.getOwner();

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());

        // no password

        given().
            queryParam(ROLE_ID, role.getId()).
        when().
            put(PATH_EVENT_ID_ACCOUNT_ID, event.getId(), user1.getId()).
        then().
            statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());

        // incorrect password

        given().
            queryParam(ROLE_ID, role.getId()).
            queryParam(EVENT_PASSWORD, event.getPassword() + "!").
        when().
            put(PATH_EVENT_ID_ACCOUNT_ID, event.getId(), user1.getId()).
        then().
            statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.eventService.getWithAccounts(event.getId()).getAccounts().size());
        assertEquals(1, this.accountService.getWithEvents(owner.getId()).getEvents().size());
        assertEquals(0, this.accountService.getWithEvents(user1.getId()).getEvents().size());
    }


}