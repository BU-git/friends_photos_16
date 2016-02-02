package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.rest.dto.EventInfo;
import com.bionic.fp.web.rest.dto.EventInput;
import com.bionic.fp.web.rest.v1.EventController;
import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.utils.TokenUtils;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.Constants.RoleConstants.MEMBER;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateless-header-spring-security.xml"})
public class EventRestControllerIT extends AbstractIT {

    @Resource private FilterChainProxy springSecurityFilterChain;
    @Resource private TokenUtils tokenUtils;
    @Value("${token.header}") private String tokenHeader;

    @Override
    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build());
    }

    @Test
    public void testFindEventByIdSuccess() {
        Account owner = getRegularUser();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = given()
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        EventInfo eventInfo = response.as(EventInfo.class);
        assertEqualsEvent(event, eventInfo);

        event = getSavedEventMax(owner);

        response = given()
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        eventInfo = response.as(EventInfo.class);
        assertEqualsEvent(event, eventInfo);
    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
        long id = Long.MAX_VALUE;

        given()
            .header(tokenHeader, getToken(getRegularUser()))
        .when()
            .get(API+V1+EVENTS + EVENT_ID, id)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindEventsByNameSuccess() {
        Account owner = getRegularUser();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        event1.setName("The first event this winter");
        event2.setName("The second event this winter");
        event3.setName("The third event this winter");
        event1 = getSaved(event1, owner);
        event2 = getSaved(event2, owner);
        event3 = getSaved(event3, owner);

        MockMvcResponse response = given()
            .param(EVENT.NAME, "event")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(3, lists.getEvents().size());
        assertEqualsEvent(lists.getEvents(), event1, event2, event3);

        response = given()
            .param(EVENT.NAME, "d e")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(2, lists.getEvents().size());
        assertEqualsEvent(lists.getEvents(), event2, event3);
    }

    @Test
    public void testFindEventsByDescriptionSuccess() {
        Account owner = getRegularUser();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        event1.setDescription("The first event this winter");
        event2.setDescription("The second event this winter");
        event3.setDescription("The third event this winter");
        event1 = getSaved(event1, owner);
        event2 = getSaved(event2, owner);
        event3 = getSaved(event3, owner);

        MockMvcResponse response = given()
            .param(EVENT.DESCRIPTION, "event")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(3, lists.getEvents().size());
        assertEqualsEvent(lists.getEvents(), event1, event2, event3);

        response = given()
            .param(EVENT.DESCRIPTION, "d e")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(2, lists.getEvents().size());
        assertEqualsEvent(lists.getEvents(), event2, event3);
    }

    @Test
    @Ignore
    public void testFindEventsByNameAndDescriptionSuccess() {
        Account owner = getRegularUser();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        event1.setName("The first event is the best");
        event2.setName("The second event");
        event3.setName("The third event");
        event1.setDescription("Description of the first event");
        event2.setDescription("Description of the second event");
        event3.setDescription("Description of the third event");
        event1 = getSaved(event1, owner);
        event2 = getSaved(event2, owner);
        event3 = getSaved(event3, owner);

        MockMvcResponse response = given()
            .param(EVENT.NAME, "second")
            .param(EVENT.DESCRIPTION, "third")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(2, lists.getEvents().size()); // todo: fixme OR != AND
        assertEqualsEvent(lists.getEvents(), event2, event3);

        response = given()
            .param(EVENT.NAME, "best")
            .param(EVENT.DESCRIPTION, "second")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(2, lists.getEvents().size());
        assertEqualsEvent(lists.getEvents(), event1, event2);

        response = given()
            .param(EVENT.NAME, "event")
            .param(EVENT.DESCRIPTION, "event")
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getEvents());
        assertEquals(3, lists.getEvents().size());
        assertEqualsEvent(lists.getEvents(), event1, event2, event3);
    }

    private void assertEqualsEvent(Event expected, EventInfo actual) {
        if(expected.getId() != null) assertEquals(expected.getId(), actual.getId());
        else assertNull(actual.getId());
        if(expected.getName() != null) assertEquals(expected.getName(), actual.getName());
        else assertNull(actual.getName());
        if(expected.getDescription() != null) assertEquals(expected.getDescription(), actual.getDescription());
        else assertNull(actual.getDescription());
        if(expected.getEventType() != null && expected.getEventType().getId() != null)
            assertEquals(expected.getEventType().getId(), actual.getTypeId());
        else assertNull(actual.getTypeId());
        assertEquals(expected.isGeoServicesEnabled(), actual.getGeo());
        assertEquals(expected.isVisible(), actual.getVisible());
        if(expected.getRadius() != null) assertEquals(expected.getRadius(), actual.getRadius());
        else assertNull(actual.getRadius());
        if(expected.getLatitude() != null) assertEquals(expected.getLatitude(), actual.getLatitude());
        else assertNull(actual.getLatitude());
        if(expected.getLongitude() != null) assertEquals(expected.getLongitude(), actual.getLongitude());
        else assertNull(actual.getLongitude());
    }

    private void assertEqualsEvent(final List<EventInfo> actuals, final Event ... events) {
        if(events == null || events.length == 0) {
            return;
        }
        if(actuals == null || actuals.isEmpty() || events.length != actuals.size()) {
            fail();
        }
        for (Event event : events) {
            Optional<EventInfo> optional = actuals.stream().parallel()
                    .filter(e -> event.getId().equals(e.getId())).findFirst();
            if(optional.isPresent()) {
                assertEqualsEvent(event, optional.get());
            } else {
                fail();
            }
        }

    }

    @Test
    public void testSaveEventSuccess() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();

        EventInput eventDto = new EventInput(getNewEventMax());

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_CREATED);

        List<Event> events = this.accountEventService.getEvents(owner.getId());
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

        // without name, description, type
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description, type
        eventDto.setName("NY 2016");
        eventDto.setDescription(null);
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, description
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setEventTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, type
        eventDto.setName(null);
        eventDto.setDescription("Happy New Year!");
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name
        eventDto.setName(null);
        eventDto.setDescription("Happy New Year!");
        eventDto.setEventTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without type
        eventDto.setName("NY 2016");
        eventDto.setDescription("Happy New Year!");
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description
        eventDto.setName("NY 2016");
        eventDto.setDescription(null);
        eventDto.setEventTypeId(privateEvent.getId());

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

//    @Test
//    public void testSaveEventWithoutValidOwnerIdShouldReturnUnauthorized() {
//        EventInput eventDto = new EventInput(getNewEventMin());
//
//        // without owner ID
//        given()
//            .body(eventDto)
//            .contentType(JSON)
//        .when()
//            .post(API+V1+EVENTS)
//        .then()
//            .statusCode(SC_UNAUTHORIZED);
//
//        // with a non-existent ID
//        given()
//            .body(eventDto)
//            .contentType(JSON)
//        .when()
//            .post(API+V1+EVENTS)
//        .then()
////            .statusCode(SC_UNAUTHORIZED); // todo: fixme
//            .statusCode(SC_BAD_REQUEST);
//    }

    @Test
    public void testRemoveEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        given()
            .header(tokenHeader, this.tokenUtils.generateToken(new User(owner)))
        .when()
            .delete(API+V1+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void testRemoveEventByIdShouldReturnUnauthorized() {
        Event event = getSavedEventMin(getSavedAccount());

        // no session
        when()
            .delete(API+V1+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testRemoveEventByIdShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        // user does not exist
        given()
            .header(tokenHeader, getToken(getNewEmailAccount(Long.MAX_VALUE)))
        .when()
            .delete(API+V1+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // event does not exist
        given()
            .header(tokenHeader, getToken(owner))
        .when()
            .delete(API+V1+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testRemoveEventByIdShouldReturnNotFound() {
        given()
            .header(tokenHeader, getToken(getRegularUser()))
        .when()
            .delete(API+V1+EVENTS + EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

//    @Test @Ignore // todo: there is no necessary role
//    public void testRemoveEventByIdShouldReturnBadRequest() {
//         when()
//            .delete(API+V1+EVENTS + EVENT_ID, Long.MAX_VALUE)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//    }

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

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, getToken(owner))
        .when()
            .put(API+V1+EVENTS + EVENT_ID, event.getId())
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
            .header(tokenHeader, getToken(owner))
        .when()
            .put(API+V1+EVENTS + EVENT_ID, event.getId())
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
            .header(tokenHeader, getToken(getRegularUser()))
        .when()
            .put(API+V1+EVENTS + EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

//    @Test @Ignore // todo: there is no necessary role
//    public void testUpdateEventShouldReturnBadRequest() {
//        EventInput eventDto = new EventInput();
//        given()
//            .body(eventDto)
//            .contentType(JSON)
//            .header(tokenHeader, getToken(getRegularUser()))
//        .when()
//            .put(API+V1+EVENTS + EVENT_ID, Long.MAX_VALUE)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//    }

//    @Test
//    public void testUpdateEventShouldReturnUnauthorized() {
//        Event event = getSavedEventMin(getSavedAccount());
//        EventInput eventDto = new EventInput(updateEvent(getNewEventMax()));
//
//        // no session
//
//        given()
//            .body(eventDto)
//            .contentType(JSON)
//        .when()
//            .put(API+V1+EVENTS + EVENT_ID, event.getId())
//        .then()
//            .statusCode(SC_UNAUTHORIZED);
//    }

    @Test
    public void testUpdateEventShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        EventInput eventDto = new EventInput(updateEvent(getNewEventMax()));

        // the user does not exist

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, getToken(getNewEmailAccount(Long.MAX_VALUE)))
        .when()
            .put(API+V1+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // the event does not exist

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, getToken(owner))
        .when()
            .put(API+V1+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_FORBIDDEN);

        // no permission

        Account user = getSavedAccount();

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user.getId()).size());

        this.eventService.addOrUpdateAccountToEvent(user.getId(), event.getId(), MEMBER, null);

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user.getId()).size());

        given()
            .body(eventDto)
            .contentType(JSON)
            .header(tokenHeader, getToken(user))
        .when()
            .put(API+V1+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testAddAccountToEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(getNewEventMax(), owner);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        given()
            .header(tokenHeader, getToken(user1))
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        given()
            .header(tokenHeader, getToken(user2))
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user2.getId()).size());

        Event newEvent = getSaved(getNewEventMax(), user2);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        given()
            .header(tokenHeader, getToken(owner))
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(2, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        given()
            .header(tokenHeader, getToken(user1))
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(3, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());
    }

    @Test
    public void testAddAccountToPrivateEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
            header(tokenHeader, getToken(user1)).
        when().
            post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
            header(tokenHeader, getToken(user2)).
        when().
            post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user2.getId()).size());

        Event newEvent = getSaved(setPrivate(getNewEventMax()), user2);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
            header(tokenHeader, getToken(owner)).
        when().
            post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(2, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        given().
            queryParam(EVENT.PASSWORD, event.getPassword()).
                header(tokenHeader, getToken(user1)).
        when().
            post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId(), user1.getId()).
        then().
            statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(3, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());
    }

    @Test
    public void testAddAccountToPrivateEventShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Event event = getSaved(setPrivate(getNewEventMax()), owner);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());

        // no password

        given().
            header(tokenHeader, getToken(user1)).
        when().
            post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());

        // incorrect password

        given().
            queryParam(EVENT.PASSWORD, event.getPassword() + "!").
            header(tokenHeader, getToken(user1)).
        when().
            post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId()).
        then().
            statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
    }

    private String getToken(final Account account) {
        return this.tokenUtils.generateToken(new User(account));
    }
}