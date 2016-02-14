package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.Constants.RoleConstants;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Coordinate;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.service.impl.SpringMethodSecurityService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.web.rest.v1.EventController;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.Constants.RoleConstants.MEMBER;
import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link EventController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-spring-security.xml"})
public class EventRestControllerIT extends AbstractIT {

    @Autowired SpringMethodSecurityService springMethodSecurityService;
    @Autowired EventController eventController;

    @Override
    @Before
    public void setUp() {
        springMethodSecurityService = Mockito.mock(SpringMethodSecurityService.class);
        ReflectionTestUtils.setField(springMethodSecurityService, "roleService", roleService);
        ReflectionTestUtils.setField(springMethodSecurityService, "accountService", accountService);
        ReflectionTestUtils.setField(eventController, "methodSecurityService", springMethodSecurityService);
//        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());
    }

    @After
    public void tearDown() throws Exception {
        softDeleteAllEvents();
    }

    //***************************************
    //                 @GET
    //***************************************


    @Test
    public void testFindEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = when()
            .get(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        EventInfo eventInfo = response.as(EventInfo.class);
        assertEquals(convert(event), eventInfo);

        event = getSavedEventMax(owner);

        response = when()
            .get(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        eventInfo = response.as(EventInfo.class);
        assertEquals(convert(event), eventInfo);

        // visible false for event2
        event.setVisible(false);
        assertFalse(this.eventDAO.update(event).isVisible());

        response = when()
            .get(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        eventInfo = response.as(EventInfo.class);
        assertEquals(convert(event), eventInfo);
    }

    @Test
    public void testFindEventByIdWithFieldsSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        String fields = "name,event_id,description";

        MockMvcResponse response = given()
            .param(PARAM.FIELDS, fields)
        .when()
            .get(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        EventInfo eventInfo = response.as(EventInfo.class);
        assertEquals(convert(event, fields), eventInfo);
    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
        when()
            .get(API+V1+EVENTS + EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindAllEvents() {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        of(event1, event2, event3).forEach(event -> save(owner, event));

        MockMvcResponse response = when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event1, event2, event3)), lists.getEvents());

        // visible false for event2
        event2.setVisible(false);
        this.eventDAO.update(event2);

        response = when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event1, event3)), lists.getEvents());

        // visible false for all events
        of(event1, event3).forEach(event -> {
            event.setVisible(false);
            this.eventDAO.update(event);
        });

        response = when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(emptyList(), lists.getEvents());
    }

    @Test
    public void testFindAllEventsWithFields() {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        of(event1, event2, event3).forEach(event -> save(owner, event));

        String fields = "name,event_id";

        MockMvcResponse response = given()
            .param(PARAM.FIELDS, fields)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event1, event2, event3), fields), lists.getEvents());
    }

    @Test
    public void testFindAllEventIds() {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        of(event1, event2, event3).forEach(event -> save(owner, event));

        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ID)
        .then()
            .statusCode(SC_OK).extract().response();

        IdLists lists = response.as(IdLists.class);

        assertEquals(of(event1, event2, event3).map(Event::getId).collect(toList()), lists.getEvents());
    }

    @Test
    public void testFindEventsByNameSuccess() {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setName("The first event starts at " + now);
        event2.setName("The second event starts at " + now);
        event3.setName("The third event starts at " + now);
        of(event1, event2, event3).forEach(event -> save(owner, event));

        MockMvcResponse response = given()
            .param(EVENT.NAME, "at " + now)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event1, event2, event3)), lists.getEvents());

        response = given()
            .param(EVENT.NAME, "d event starts at " + now)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event2, event3)), lists.getEvents());
    }

    @Test
    public void testFindEventsByDescriptionSuccess() {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setDescription("The first event starts at " + now);
        event2.setDescription("The second event starts at " + now);
        event3.setDescription("The third event starts at " + now);
        Stream.of(event1, event2, event3).forEach(event -> save(owner, event));

        MockMvcResponse response = given()
            .param(EVENT.DESCRIPTION, "at " + now)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event1, event2, event3)), lists.getEvents());

        response = given()
            .param(EVENT.DESCRIPTION, "d event starts at " + now)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event2, event3)), lists.getEvents());
    }

    @Test
    public void testFindEventsByNameAndDescriptionSuccess() {
        Account owner = getSavedAccount();
        Event event1 = getNewEventMin();
        Event event2 = getNewEventMin();
        Event event3 = getNewEventMin();
        LocalDateTime now = LocalDateTime.now();
        event1.setName("The first event starts at " + now);
        event2.setName("The second event starts at " + now);
        event3.setName("The third event starts at " + now);
        event1.setDescription("The first event starts at " + now);
        event2.setDescription("The second event starts at " + now);
        event3.setDescription("The third event starts at " + now);
        Stream.of(event1, event2, event3).forEach(event -> save(owner, event));

        MockMvcResponse response = given()
            .param(EVENT.NAME, "at " + now)
            .param(EVENT.DESCRIPTION, "at " + now)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event1, event2, event3)), lists.getEvents());

        response = given()
            .param(EVENT.NAME, "second")
            .param(EVENT.DESCRIPTION, "d event starts at " + now)
        .when()
            .get(API+V1+EVENTS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(event2)), lists.getEvents());
    }

    @Test
    public void testFindEventsByAccount() {
        Account account1 = getSavedAccount();
        Account account2 = getSavedAccount();
        List<Event> events = new ArrayList<>(5);
        IntStream.range(0, 3).forEach((i) -> events.add(getSavedEventMin(account1)));
        IntStream.range(0, 2).forEach((i) -> events.add(getSavedEventMin(account2)));

        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, account1.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().limit(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, account2.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // visible of events(1, 4) are false
        events.get(1).setVisible(false);
        events.get(4).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(1)).isVisible());
        assertFalse(this.eventDAO.update(events.get(4)).isVisible());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, account1.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().limit(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, account2.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // events(0, 3) are deleted
        this.eventDAO.setDeleted(events.get(0).getId(), true);
        this.eventDAO.setDeleted(events.get(3).getId(), true);

        response = when()
                .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, account1.getId())
                .then()
                .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1), events.get(2))), lists.getEvents());

        response = when()
                .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, account2.getId())
                .then()
                .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(4))), lists.getEvents());

        // no exists account
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(emptyList(), lists.getEvents());
    }

    @Test
    public void testFindEventsByUser() {
        Account account1 = getSavedAccount();
        Account account2 = getSavedAccount();
        List<Event> events = new ArrayList<>(5);
        IntStream.range(0, 3).forEach((i) -> events.add(getSavedEventMin(account1)));
        IntStream.range(0, 2).forEach((i) -> events.add(getSavedEventMin(account2)));

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(account1.getId());
        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().limit(3)), lists.getEvents());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(account2.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // visible of events(1, 4) are false
        events.get(1).setVisible(false);
        events.get(4).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(1)).isVisible());
        assertFalse(this.eventDAO.update(events.get(4)).isVisible());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(account1.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().limit(3)), lists.getEvents());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(account2.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // events(0, 3) are deleted
        this.eventDAO.setDeleted(events.get(0).getId(), true);
        this.eventDAO.setDeleted(events.get(3).getId(), true);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(account1.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1), events.get(2))), lists.getEvents());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(account2.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(4))), lists.getEvents());
    }

    @Test
    public void testFindEventsByAccountOwner() {
        Account account1 = getSavedAccount();
        Account account = getSavedAccount();
        List<Event> events = new ArrayList<>(5);
        IntStream.range(0, 3).forEach((i) -> events.add(getSavedEventMin(account1)));
        IntStream.range(0, 2).forEach((i) -> events.add(getSavedEventMin(account)));
        getSavedAccountEvent(account, events.get(2), getRoleMember());

        assertEqualsEntities(accountEventService.getEvents(account.getId()), events.get(2), events.get(3), events.get(4));

        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+OWNER, account.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // visible of events(4) is false
        events.get(4).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(4)).isVisible());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+OWNER, account.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // events(3) are deleted
        this.eventDAO.setDeleted(events.get(3).getId(), true);

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+OWNER, account.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(4))), lists.getEvents());
    }

    @Test
    public void testFindEventsByUserOwner() {
        Account account1 = getSavedAccount();
        Account user = getSavedAccount();
        List<Event> events = new ArrayList<>(5);
        IntStream.range(0, 3).forEach((i) -> events.add(getSavedEventMin(account1)));
        IntStream.range(0, 2).forEach((i) -> events.add(getSavedEventMin(user)));
        getSavedAccountEvent(user, events.get(2), getRoleMember());

        assertEqualsEntities(accountEventService.getEvents(user.getId()), events.get(2), events.get(3), events.get(4));

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // visible of events(4) is false
        events.get(4).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(4)).isVisible());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        // events(3) are deleted
        this.eventDAO.setDeleted(events.get(3).getId(), true);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(4))), lists.getEvents());
    }

    @Test
    public void testFindEventsByAccountAndRole() {
        Account account1 = getSavedAccount();
        Account account2 = getSavedAccount();
        List<Event> events = new ArrayList<>(5);
        IntStream.range(0, 3).forEach((i) -> events.add(getSavedEventMin(account1)));
        IntStream.range(0, 2).forEach((i) -> events.add(getSavedEventMin(account2)));
        getSavedAccountEvent(account1, events.get(3), getRoleMember());
        getSavedAccountEvent(account2, events.get(2), getRoleMember());

        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account1.getId(), RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().limit(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account1.getId(), RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(3))), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account2.getId(), RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account2.getId(), RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(2))), lists.getEvents());

        // visible of events(2, 3) are false
        events.get(2).setVisible(false);
        events.get(3).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(2)).isVisible());
        assertFalse(this.eventDAO.update(events.get(3)).isVisible());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account1.getId(), RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().limit(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account1.getId(), RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(3))), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account2.getId(), RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account2.getId(), RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(2))), lists.getEvents());

        // events(0, 3) are deleted
        this.eventDAO.setDeleted(events.get(0).getId(), true);
        this.eventDAO.setDeleted(events.get(3).getId(), true);

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account1.getId(), RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1), events.get(2))), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account1.getId(), RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(emptyList(), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account2.getId(), RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(4))), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+ACCOUNT_ID+ROLES+ROLE_ID, account2.getId(), RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(2))), lists.getEvents());
    }

    @Test
    public void testFindEventsByUserAndRole() {
        Account account1 = getSavedAccount();
        Account user = getSavedAccount();
        List<Event> events = new ArrayList<>(5);
        IntStream.range(0, 3).forEach((i) -> events.add(getSavedEventMin(account1)));
        IntStream.range(0, 2).forEach((i) -> events.add(getSavedEventMin(user)));
        getSavedAccountEvent(account1, events.get(3), getRoleMember());
        getSavedAccountEvent(user, events.get(2), getRoleMember());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());

        MockMvcResponse response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+ROLES+ROLE_ID, RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+ROLES+ROLE_ID, RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(2))), lists.getEvents());

        // visible of events(2, 3) are false
        events.get(2).setVisible(false);
        events.get(3).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(2)).isVisible());
        assertFalse(this.eventDAO.update(events.get(3)).isVisible());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+ROLES+ROLE_ID, RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream().skip(3)), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+ROLES+ROLE_ID, RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(2))), lists.getEvents());

        // events(0, 3) are deleted
        this.eventDAO.setDeleted(events.get(0).getId(), true);
        this.eventDAO.setDeleted(events.get(3).getId(), true);

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+ROLES+ROLE_ID, RoleConstants.OWNER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(4))), lists.getEvents());

        response = when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF+ROLES+ROLE_ID, RoleConstants.MEMBER)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(2))), lists.getEvents());
    }

    @Test
    public void testFindEventsInRadiusSuccess() throws Exception {
        float  e = 0.003f;    // 3m
        Account owner = getSavedAccount();
        List<Event> events = getSavedFiveEventsWithStep100m(owner);

        LocationDto locationDto = new LocationDto();
        locationDto.setLocation(events.get(2).getLocation());
        locationDto.setRadius((0.2f+e));

        MockMvcResponse response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RADIUS)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream()), lists.getEvents());

        locationDto.setRadius((0.1f+e));

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RADIUS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1), events.get(2), events.get(3))), lists.getEvents());

        locationDto.setLocation(events.get(1).getLocation());

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RADIUS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(0), events.get(1), events.get(2))), lists.getEvents());

        locationDto.setRadius((0.2f+e));

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RADIUS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(0), events.get(1), events.get(2), events.get(3))), lists.getEvents());

        // visible of events(2, 3) are false
        events.get(2).setVisible(false);
        events.get(3).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(2)).isVisible());
        assertFalse(this.eventDAO.update(events.get(3)).isVisible());

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RADIUS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(0), events.get(1))), lists.getEvents());

        // events(0) are deleted
        this.eventDAO.setDeleted(events.get(0).getId(), true);

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RADIUS)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1))), lists.getEvents());

    }

    @Test
    public void testFindEventsInRangeSuccess() throws Exception {
        float e = 0.003f;    // 3m
        Account owner = getSavedAccount();
        List<Event> events = Stream.of(
                new Coordinate(50.01, 30.06),
                new Coordinate(50.02, 30.07),
                new Coordinate(50.03, 30.08),
                new Coordinate(50.04, 30.09),
                new Coordinate(50.05, 30.10)
        ).sequential().map(coordinate -> {
            Event event = getNewEventMin();
            event.setGeoServicesEnabled(true);
            event.setLocation(coordinate);
            return save(owner, event);
        }).collect(toList());

        LocationDto locationDto = new LocationDto();
        locationDto.setSw(new Coordinate(50.01, 30.06));
        locationDto.setNe(new Coordinate(50.05, 30.10));

        MockMvcResponse response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RANGE)
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertEquals(convert(events.stream()), lists.getEvents());

        locationDto.setSw(new Coordinate(50.02, 30.07));
        locationDto.setNe(new Coordinate(50.04, 30.09));

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RANGE)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1), events.get(2), events.get(3))), lists.getEvents());

        locationDto.setSw(new Coordinate(50.01, 30.06));
        locationDto.setNe(new Coordinate(50.03, 30.08));

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RANGE)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(0), events.get(1), events.get(2))), lists.getEvents());

        locationDto.setSw(new Coordinate(50.01, 30.06));
        locationDto.setNe(new Coordinate(50.04, 30.09));

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RANGE)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(0), events.get(1), events.get(2), events.get(3))), lists.getEvents());

        // visible of events(2, 3) are false
        events.get(2).setVisible(false);
        events.get(3).setVisible(false);
        assertFalse(this.eventDAO.update(events.get(2)).isVisible());
        assertFalse(this.eventDAO.update(events.get(3)).isVisible());

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RANGE)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(0), events.get(1))), lists.getEvents());

        // events(0) are deleted
        this.eventDAO.setDeleted(events.get(0).getId(), true);

        response = given()
            .contentType(JSON)
            .body(locationDto)
        .when()
            .get(API+V1+EVENTS+LOCATION+RANGE)
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertEquals(convert(of(events.get(1))), lists.getEvents());
    }


    //***************************************
    //                 @POST
    //***************************************


    @Test
    public void testSaveEventSuccess() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();
        EventInput eventDto = new EventInput(getNewEventMax());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());

        MockMvcResponse response = given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_CREATED).extract().response();

        IdInfo idInfo = response.as(IdInfo.class);
        Event actual = eventService.get(idInfo.getId());
        assertNotNull(actual);

        assertEquals(actual.getName(), eventDto.getName());
        assertEquals(actual.getDescription(), eventDto.getDescription());
        assertEquals(actual.getEventType(), privateEvent);
        assertEquals(actual.getLocation(), eventDto.getLocation());
        assertEquals(actual.isGeoServicesEnabled(), true);
        // by default
        assertTrue(actual.isVisible());
        assertFalse(actual.isDeleted());
        assertNotNull(actual.getCreated());
        assertNull(actual.getModified());

        Account actualOwner = getEventOwner(actual.getId());
        assertEquals(actualOwner.getId(), owner.getId());
        assertEquals(actualOwner.getEmail(), owner.getEmail());
        assertEquals(actualOwner.getUserName(), owner.getUserName());
    }

    @Test
    public void testSaveEventShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        EventType privateEvent = getPrivateEventType();
        EventInput eventDto = new EventInput(getNewEventMax());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());

        // without name, description, type
        eventDto.setName(null);
        eventDto.setDescription(null);
        eventDto.setEventTypeId(null);

        given()
            .body(eventDto)
            .contentType(JSON)
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
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testSaveEventWithoutValidOwnerIdShouldReturnBadRequest() {
        EventInput eventDto = new EventInput(getNewEventMin());

        // without owner ID
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // with a non-existent ID
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .post(API+V1+EVENTS)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testAddUserToPublicEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = save(owner, getNewEventMax());

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user1.getId());
        when()
            .post(API+V1+EVENTS+EVENT_ID+ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user2.getId());
        when()
            .post(API+V1+EVENTS+EVENT_ID+ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user2.getId()).size());

        Event newEvent = save(user2, getNewEventMax());

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(2, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user1.getId());
        when()
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
    public void testAddUserToPrivateEventSuccess() {
        Account owner = getSavedAccount();
        Account user1 = getSavedAccount();
        Account user2 = getSavedAccount();
        Event event = save(owner, setPrivate(getNewEventMax()));

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user1.getId());
        given()
            .queryParam(EVENT.PASSWORD, event.getPassword())
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(2, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user2.getId());
        given()
            .queryParam(EVENT.PASSWORD, event.getPassword())
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user2.getId()).size());

        Event newEvent = save(user2, setPrivate(getNewEventMax()));

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        given()
            .queryParam(EVENT.PASSWORD, event.getPassword())
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId())
        .then()
            .statusCode(SC_CREATED);

        assertEquals(3, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(2, this.accountEventService.getAccounts(newEvent.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(user1.getId()).size());
        assertEquals(2, this.accountEventService.getEvents(user2.getId()).size());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user1.getId());
        given()
            .queryParam(EVENT.PASSWORD, event.getPassword()).
        when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, newEvent.getId(), user1.getId())
        .then()
            .statusCode(SC_CREATED);

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
        Event event = save(owner, setPrivate(getNewEventMax()));

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());

        // no password
        when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());

        // incorrect password
        given()
            .queryParam(EVENT.PASSWORD, event.getPassword() + "!")
        .when()
            .post(API+V1+EVENTS + EVENT_ID + ACCOUNTS, event.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);

        assertEquals(1, this.accountEventService.getAccounts(event.getId()).size());
        assertEquals(1, this.accountEventService.getEvents(owner.getId()).size());
        assertEquals(0, this.accountEventService.getEvents(user1.getId()).size());
    }












    //***************************************
    //                 @PUT
    //***************************************


    @Test
    public void testUpdateEventSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMax(owner);

        EventInput eventDto = new EventInput(update(event));

        assertNotEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        // todo: when there will be more types
//        assertNotEquals(event.getEventType().getId(), eventDto.getEventTypeId());
        assertNotEquals(event.getLocation(), eventDto.getLocation());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo()); // todo

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK);

        event = this.eventService.get(event.getId());

        assertEquals(event.getName(), eventDto.getName());
        assertEquals(event.getDescription(), eventDto.getDescription());
        assertEquals(event.getEventType().getId(), eventDto.getEventTypeId());
        assertEquals(event.getLocation(), eventDto.getLocation());
        assertEquals(event.isGeoServicesEnabled(), eventDto.getGeo());

        eventDto = new EventInput();
        eventDto.setName("NY 2020");

        assertNotEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        // todo: when there will be more types
//        assertNotEquals(event.getEventType().getId(), eventDto.getEventTypeId());
        assertNotEquals(event.getLocation(), eventDto.getLocation());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+V1+EVENTS + EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK);

        event = this.eventService.get(event.getId());

        assertEquals(event.getName(), eventDto.getName());
        assertNotEquals(event.getDescription(), eventDto.getDescription());
        assertNotEquals(event.getEventType().getId(), eventDto.getEventTypeId());
        assertNotEquals(event.getLocation(), eventDto.getLocation());
        assertNotEquals(event.isGeoServicesEnabled(), eventDto.getGeo());
    }

    @Test
    public void testUpdateEventShouldReturnNotFound() {
        EventInput eventDto = new EventInput();

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+V1+EVENTS+EVENT_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+V1+EVENTS+EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testUpdateEventShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        EventInput eventDto = new EventInput(update(getNewEventMax()));

        // the user does not exist or the user doesn't belong to the event
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(Long.MAX_VALUE);
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+V1+EVENTS+EVENT_ID, event.getId())
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

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        given()
            .body(eventDto)
            .contentType(JSON)
        .when()
            .put(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }










    //***************************************
    //                 @DELETE
    //***************************************


    @Test
    public void testRemoveEventByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        when()
            .delete(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_NO_CONTENT);

        // and the following queries should return NO CONTENT
        when()
            .delete(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void testRemoveEventByIdShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        // the user does not exist or the user doesn't belong to the event
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(Long.MAX_VALUE);
        when()
            .delete(API+V1+EVENTS + EVENT_ID, event.getId())
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

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        when()
            .delete(API+V1+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    private EventInfo convert(final Event event) {
        EventInfo result = new EventInfo(event);
        if(result.getDate() != null) {
            result.setDate(LocalDateTime.parse(result.getDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        if(result.getExpireDate() != null) {
            result.setExpireDate(LocalDateTime.parse(result.getExpireDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        return result;
    }

    private List<EventInfo> convert(final Stream<Event> eventStream) {
        return eventStream.unordered().map(this::convert).collect(toList());
    }

    private EventInfo convert(final Event event, final String fields) {
        EventInfo result = EventInfo.Transformer.transform(event, fields);
        if(result.getDate() != null) {
            result.setDate(LocalDateTime.parse(result.getDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        if(result.getExpireDate() != null) {
            result.setExpireDate(LocalDateTime.parse(result.getExpireDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        return result;
    }

    private List<EventInfo> convert(final Stream<Event> eventStream, String fields) {
        return eventStream.unordered().map(event -> convert(event, fields)).collect(toList());
    }
}