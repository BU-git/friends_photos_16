package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Group;
import com.bionic.fp.domain.GroupType;
import com.bionic.fp.rest.dto.GroupCreateDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.GroupService;
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
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link GroupController}
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class GroupControllerIT {

    private static final String GROUPS = "/groups";

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        this.groupService.clear();
    }

    @Test
    public void testSaveGroupSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        owner.setActive(true);
        owner.setGuest(false);
        Long ownerId = this.accountService.addAccount(owner);

        GroupCreateDTO groupDTO = new GroupCreateDTO();
        groupDTO.setName("NY 2016");
        groupDTO.setDescription("Happy New Year!");
        groupDTO.setType(GroupType.PRIVATE);
        groupDTO.setOwnerId(ownerId);
        groupDTO.setVisible(true);
        groupDTO.setLatitude(0.0);
        groupDTO.setLongitude(0.0);

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_OK);
    }

    @Test
    public void testSaveGroupShouldReturnBadRequest() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        owner.setActive(true);
        owner.setGuest(false);
        Long ownerId = this.accountService.addAccount(owner);

        GroupCreateDTO groupDTO = new GroupCreateDTO();
        groupDTO.setOwnerId(ownerId);
        groupDTO.setVisible(true);
        groupDTO.setLatitude(0.0);
        groupDTO.setLongitude(0.0);

        // without name, description, type
        groupDTO.setName(null);
        groupDTO.setType(null);
        groupDTO.setDescription(null);

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description, type
        groupDTO.setName("NY 2016");
        groupDTO.setType(null);
        groupDTO.setDescription(null);

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, description
        groupDTO.setName(null);
        groupDTO.setType(GroupType.PRIVATE);
        groupDTO.setDescription(null);

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name, type
        groupDTO.setName(null);
        groupDTO.setType(null);
        groupDTO.setDescription("Happy New Year!");

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without name
        groupDTO.setName(null);
        groupDTO.setType(GroupType.PRIVATE);
        groupDTO.setDescription("Happy New Year!");

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without type
        groupDTO.setName("NY 2016");
        groupDTO.setType(null);
        groupDTO.setDescription("Happy New Year!");

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // without description
        groupDTO.setName("NY 2016");
        groupDTO.setType(GroupType.PRIVATE);
        groupDTO.setDescription(null);

        given()
            .body(groupDTO)
            .contentType(JSON)
        .when()
            .post(GROUPS)
        .then()
            .statusCode(SC_BAD_REQUEST);

    }

    @Test
    public void testRemoveGroupByIdSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        owner.setActive(true);
        owner.setGuest(false);

        Long ownerId = this.accountService.addAccount(owner);

        Group group = new Group();
        group.setName("NY 2016");
        group.setDescription("Happy New Year!");
        group.setGroupType(GroupType.PRIVATE);
        group.setOwner(owner);
        group.setVisible(true);

        Long groupId = this.groupService.addGroup(group);

        when()
            .delete(GROUPS + "/{id}", group.getId())
        .then()
            .statusCode(SC_OK);
    }

    @Test
    public void testRemoveGroupByIdShouldReturnNotFound() {
        when()
            .delete(GROUPS + "/{id}", "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);

    // todo: throw an exception inside and handle it
//        when()
//            .delete(GROUPS + "/{id}", "99999")
//        .then()
//            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindGroupByIdSuccess() {
        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        owner.setActive(true);
        owner.setGuest(false);

        Long ownerId = this.accountService.addAccount(owner);

        Group group = new Group();
        group.setName("NY 2016");
        group.setDescription("Happy New Year!");
        group.setGroupType(GroupType.PRIVATE);
        group.setOwner(owner);
        group.setVisible(true);

        Long groupId = this.groupService.addGroup(group);

        when()
            .get(GROUPS + "/{id}", group.getId())
        .then()
            .statusCode(SC_OK)
            .body("id.toString()", is(group.getId().toString()))
            .body("name", is(group.getName()))
            .body("type", is(group.getGroupType().toString()))
            .body("description", is(group.getDescription()))
            .body("date", is(group.getDate().format(LOCAL_DATE_TIME)))
//            .body("expire_date", is(group.getExpireDate().toString()))
            .body("latitude", is(group.getLatitude()))
            .body("longitude", is(group.getLongitude()))
            .body("owner.id.toString()", is(group.getOwner().getId().toString()))
            .body("owner.user_name", is(group.getOwner().getUserName()))
            .body("owner.email", is(group.getOwner().getEmail()))
            .body("owner.profile_image_url", is(group.getOwner().getProfileImageUrl()));
    }
}