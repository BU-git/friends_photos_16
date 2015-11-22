//package com.bionic.fp.rest;
//
//import com.bionic.fp.domain.Account;
//import com.bionic.fp.domain.Event;
//import com.bionic.fp.domain.EventType;
//import com.bionic.fp.rest.dto.GroupCreateDTO;
//import com.bionic.fp.rest.dto.GroupUpdateDTO;
//import com.bionic.fp.service.AccountEventService;
//import com.bionic.fp.service.AccountService;
//import com.bionic.fp.service.GroupService;
//import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;
//import static com.jayway.restassured.http.ContentType.JSON;
//import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
//import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
//import static org.apache.http.HttpStatus.SC_NOT_FOUND;
//import static org.apache.http.HttpStatus.SC_OK;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.*;
//
///**
// * This is an integration test that verifies {@link GroupController}
// *
// * @author Sergiy Gabriel
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration("classpath:spring/test-root-context.xml")
//public class GroupControllerIT {
//
//    private static final String GROUPS = "/groups";
//
//    @Autowired
//    private GroupService groupService;
//
//    @Autowired
//    private AccountService accountService;
//
//    @Autowired
//    private AccountEventService accountEventService;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Before
//    public void setUp() {
//        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
////        this.groupService.clear();
//    }
//
//    @Test
//    public void testSaveGroupSuccess() {
//        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
//        owner.setActive(true);
//        owner.setGuest(false);
//        Long ownerId = this.accountService.addAccount(owner);
//
//        GroupCreateDTO groupDTO = new GroupCreateDTO();
//        groupDTO.setName("NY 2016");
//        groupDTO.setDescription("Happy New Year!");
////        groupDTO.setType(EventType.PRIVATE);
//        groupDTO.setOwnerId(ownerId);
//        groupDTO.setVisible(true);
//        groupDTO.setLatitude(0.0);
//        groupDTO.setLongitude(0.0);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_OK);
//
//        Account account = this.accountService.getByIdWithGroups(ownerId);
////        Long connId = account.getGroupConnections().get(0).getId();
////        Long groupId = this.accountEventService.getByIdWithAccountAndGroup(connId).getEvent().getId();
//        Event event = this.groupService.getByIdWithOwner(groupId);
//
//        assertEquals(event.getOwner().getId(), ownerId);
//        assertEquals(event.getName(), groupDTO.getName());
//        assertEquals(event.getDescription(), groupDTO.getDescription());
//        assertEquals(event.getEventType(), groupDTO.getType());
//        assertEquals(event.getLatitude(), groupDTO.getLatitude());
//        assertEquals(event.getLongitude(), groupDTO.getLongitude());
//        assertEquals(event.isVisible(), groupDTO.isVisible());
//    }
//
//    @Test
//    public void testSaveGroupShouldReturnBadRequest() {
//        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
//        owner.setActive(true);
//        owner.setGuest(false);
//        Long ownerId = this.accountService.addAccount(owner);
//
//        GroupCreateDTO groupDTO = new GroupCreateDTO();
//        groupDTO.setOwnerId(ownerId);
//        groupDTO.setVisible(true);
//        groupDTO.setLatitude(0.0);
//        groupDTO.setLongitude(0.0);
//
//        // without name, description, type
//        groupDTO.setName(null);
//        groupDTO.setType(null);
//        groupDTO.setDescription(null);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // without description, type
//        groupDTO.setName("NY 2016");
//        groupDTO.setType(null);
//        groupDTO.setDescription(null);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // without name, description
//        groupDTO.setName(null);
//        groupDTO.setType(EventType.PRIVATE);
//        groupDTO.setDescription(null);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // without name, type
//        groupDTO.setName(null);
//        groupDTO.setType(null);
//        groupDTO.setDescription("Happy New Year!");
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // without name
//        groupDTO.setName(null);
//        groupDTO.setType(EventType.PRIVATE);
//        groupDTO.setDescription("Happy New Year!");
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // without type
//        groupDTO.setName("NY 2016");
//        groupDTO.setType(null);
//        groupDTO.setDescription("Happy New Year!");
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // without description
//        groupDTO.setName("NY 2016");
//        groupDTO.setType(EventType.PRIVATE);
//        groupDTO.setDescription(null);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//    }
//
//    @Test
//    public void testSaveGroupWithoutValidOwnerIdShouldReturnBadRequest() {
//        GroupCreateDTO groupDTO = new GroupCreateDTO();
//        groupDTO.setName("NY 400");
//        groupDTO.setDescription("Happy New Year!");
//        groupDTO.setType(EventType.PRIVATE);
//        groupDTO.setVisible(true);
//
//        // without owner ID
//        groupDTO.setOwnerId(null);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // with a non-existent ID
//        groupDTO.setOwnerId(Long.MAX_VALUE);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .post(GROUPS)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//    }
//
//    @Test
//    public void testRemoveGroupByIdSuccess() {
//        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
//        owner.setActive(true);
//        owner.setGuest(false);
//
//        Long ownerId = this.accountService.addAccount(owner);
//
//        Event event = new Event();
//        event.setName("NY 2016");
//        event.setDescription("Happy New Year!");
//        event.setEventType(EventType.PRIVATE);
//        event.setVisible(true);
//
//        Long groupId = this.groupService.createGroup(ownerId, event);
//
//        when()
//            .delete(GROUPS + "/{id}", event.getId())
//        .then()
//            .statusCode(SC_OK);
//    }
//
//    @Test
//    public void testRemoveGroupByIdShouldReturnNotFound() {
//        when()
//            .delete(GROUPS + "/{id}", "1abc")
//        .then()
//            .statusCode(SC_NOT_FOUND);
//
//    // todo: throw an exception inside and handle it
////        when()
////            .delete(GROUPS + "/{id}", "99999")
////        .then()
////            .statusCode(SC_NOT_FOUND);
//    }
//
//    @Test
//    public void testFindGroupByIdSuccess() {
//        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
//        owner.setActive(true);
//        owner.setGuest(false);
//
//        Long ownerId = this.accountService.addAccount(owner);
//
//        Event event = new Event();
//        event.setName("NY 2016");
//        event.setDescription("Happy New Year!");
//        event.setEventType(EventType.PRIVATE);
//        event.setVisible(true);
//
//        Long groupId = this.groupService.createGroup(ownerId, event);
//
//        when()
//            .get(GROUPS + "/{id}", event.getId())
//        .then()
//            .statusCode(SC_OK)
//            .body("id.toString()", is(event.getId().toString()))
//            .body("name", is(event.getName()))
//            .body("type", is(event.getEventType().toString()))
//            .body("description", is(event.getDescription()))
//            .body("date", is(event.getDate().format(LOCAL_DATE_TIME)))
////            .body("expire_date", is(event.getExpireDate().toString()))
//            .body("latitude", is(event.getLatitude()))
//            .body("longitude", is(event.getLongitude()))
//            .body("owner.id.toString()", is(event.getOwner().getId().toString()))
//            .body("owner.user_name", is(event.getOwner().getUserName()))
//            .body("owner.email", is(event.getOwner().getEmail()))
//            .body("owner.profile_image_url", is(event.getOwner().getProfileImageUrl()));
//    }
//
//    @Test
//    public void testUpdateGroupSuccess() {
//        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
//        owner.setActive(true);
//        owner.setGuest(false);
//        Long ownerId = this.accountService.addAccount(owner);
//
//        Event event = new Event();
//        event.setName("NY 2015");
//        event.setDescription("Happy New Year!");
//        event.setEventType(EventType.PRIVATE);
//        event.setVisible(true);
//        Long groupId = this.groupService.createGroup(ownerId, event);
//
//        GroupUpdateDTO groupDTO = new GroupUpdateDTO();
//        groupDTO.setId(groupId);
//        groupDTO.setName("NY 2016");
//        groupDTO.setDescription("Happy New Year!!!");
//        groupDTO.setType(EventType.VIEW_OPEN);
//        groupDTO.setVisible(false);
//        groupDTO.setLatitude(40.0);
//        groupDTO.setLongitude(40.0);
//
//        assertNotEquals(event.getName(), groupDTO.getName());
//        assertNotEquals(event.getDescription(), groupDTO.getDescription());
//        assertNotEquals(event.getEventType(), groupDTO.getType());
//        assertNotEquals(event.getLatitude(), groupDTO.getLatitude());
//        assertNotEquals(event.getLongitude(), groupDTO.getLongitude());
//        assertNotEquals(event.isVisible(), groupDTO.getVisible());
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .put(GROUPS)
//        .then()
//            .statusCode(SC_OK);
//
//        event = this.groupService.getByIdWithOwner(groupId);
//
//        assertEquals(event.getName(), groupDTO.getName());
//        assertEquals(event.getDescription(), groupDTO.getDescription());
//        assertEquals(event.getEventType(), groupDTO.getType());
//        assertEquals(event.getLatitude(), groupDTO.getLatitude());
//        assertEquals(event.getLongitude(), groupDTO.getLongitude());
//        assertEquals(event.isVisible(), groupDTO.getVisible());
//
//
//        groupDTO = new GroupUpdateDTO();
//        groupDTO.setId(groupId);
//        groupDTO.setName("NY 2017");
//
//        assertNotEquals(event.getName(), groupDTO.getName());
//        assertNotEquals(event.getDescription(), groupDTO.getDescription());
//        assertNotEquals(event.getEventType(), groupDTO.getType());
//        assertNotEquals(event.getLatitude(), groupDTO.getLatitude());
//        assertNotEquals(event.getLongitude(), groupDTO.getLongitude());
//        assertNotEquals(event.isVisible(), groupDTO.getVisible());
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .put(GROUPS)
//        .then()
//            .statusCode(SC_OK);
//
//        event = this.groupService.getByIdWithOwner(groupId);
//
//        assertEquals(event.getName(), groupDTO.getName());
//        assertNotEquals(event.getDescription(), groupDTO.getDescription());
//        assertNotEquals(event.getEventType(), groupDTO.getType());
//        assertNotEquals(event.getLatitude(), groupDTO.getLatitude());
//        assertNotEquals(event.getLongitude(), groupDTO.getLongitude());
//        assertNotEquals(event.isVisible(), groupDTO.getVisible());
//    }
//
//    @Test
//    public void testUpdateGroupShouldReturnNotFound() {
//        GroupUpdateDTO groupDTO = new GroupUpdateDTO();
//        groupDTO.setId(null);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .put(GROUPS)
//        .then()
//            .statusCode(SC_NOT_FOUND);
//
//        groupDTO.setId(Long.MAX_VALUE);
//
//        given()
//            .body(groupDTO)
//            .contentType(JSON)
//        .when()
//            .put(GROUPS)
//        .then()
//            .statusCode(SC_NOT_FOUND);
//    }
//}