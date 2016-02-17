package com.bionic.fp.web.rest.v1;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.Constants.RestConstants.PHOTO;
import com.bionic.fp.domain.*;
import com.bionic.fp.service.impl.SpringMethodSecurityService;
import com.bionic.fp.web.rest.dto.*;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link PhotoController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-spring-security.xml"})
public class PhotoRestControllerIT extends AbstractIT {

    private static final String TEST_IMAGE_FILE_NAME = "test-img/BearGrylls.JPG";
    @Autowired SpringMethodSecurityService springMethodSecurityService;
    @Autowired PhotoController photoController;

    @Override
    @Before
    public void setUp() {
        springMethodSecurityService = Mockito.mock(SpringMethodSecurityService.class);
        ReflectionTestUtils.setField(springMethodSecurityService, "roleService", roleService);
        ReflectionTestUtils.setField(springMethodSecurityService, "accountService", accountService);
        ReflectionTestUtils.setField(photoController, "methodSecurityService", springMethodSecurityService);
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());
    }


    //***************************************
    //                 @GET
    //***************************************


    @Test
    public void testFindPhotoByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo = getSavedPhoto(event, owner);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        assertEquals(convert(photo), response.as(PhotoInfo.class));
    }

    @Test
    public void testFindPhotoByIdShouldReturnNotFound() {
        when()
            .get(API+V1+PHOTOS+PHOTO_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);

        when()
            .get(API+V1+PHOTOS+PHOTO_ID, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindPhotoFileByIdSuccess() throws IOException {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        File file = getFileFromResources(TEST_IMAGE_FILE_NAME);
        assertNotNull(file);
        Photo photo = getSavedPhoto(event, owner, file);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+PHOTO_ID+FILE, photo.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        assertEquals("image/jpeg", response.contentType());
        assertEquals(file.length(), response.mockHttpServletResponse().getContentLength());
    }

    @Test
    public void testFindPhotoFileByIdShouldReturnNotFound() {
        when()
            .get(API+V1+PHOTOS+PHOTO_ID+FILE, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);

        when()
            .get(API+V1+PHOTOS+PHOTO_ID+FILE, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindAccountPhotosSuccess() {
        Account owner = getSavedAccount();
        Account another = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo0 = getSavedPhoto(event, another);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        Photo photo1 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo1)), response.as(EntityInfoLists.class).getPhotos());

        Photo photo2 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(convert(of(photo1, photo2)), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+ACCOUNT_ID, another.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo0)), response.as(EntityInfoLists.class).getPhotos());
    }

    @Test
    public void testFindAccountPhotoIdsSuccess() {
        Account owner = getSavedAccount();
        Account another = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo0 = getSavedPhoto(event, another);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        Photo photo1 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo1)), response.as(IdLists.class).getPhotos());

        Photo photo2 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(photo1, photo2)), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+ACCOUNT_ID, another.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo0)), response.as(IdLists.class).getPhotos());
    }

    @Test
    public void testFindUserPhotosSuccess() {
        Account owner = getSavedAccount();
        Account another = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo0 = getSavedPhoto(event, another);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        Photo photo1 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo1)), response.as(EntityInfoLists.class).getPhotos());

        Photo photo2 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(convert(of(photo1, photo2)), response.as(EntityInfoLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(another.getId());
        response = when()
            .get(API+V1+PHOTOS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo0)), response.as(EntityInfoLists.class).getPhotos());
    }

    @Test
    public void testFindUserPhotoIdsSuccess() {
        Account owner = getSavedAccount();
        Account another = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo0 = getSavedPhoto(event, another);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        Photo photo1 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo1)), response.as(IdLists.class).getPhotos());

        Photo photo2 = getSavedPhoto(event, owner);
        response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(photo1, photo2)), response.as(IdLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(another.getId());
        response = when()
            .get(API+V1+PHOTOS+ID+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo0)), response.as(IdLists.class).getPhotos());
    }

    @Test
    public void testFindEventPhotosSuccess() {
        Account owner = getSavedAccount();
        Event event1 = getSavedEventMin(owner);
        Event event2 = getSavedEventMin(owner);
        Photo photo0 = getSavedPhoto(event2, owner);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo0)), response.as(EntityInfoLists.class).getPhotos());

        Photo photo1 = getSavedPhoto(event1, owner);
        Photo photo2 = getSavedPhoto(event1, owner);
        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(convert(of(photo1, photo2)), response.as(EntityInfoLists.class).getPhotos());
    }

    @Test
    public void testFindEventPhotoIdsSuccess() {
        Account owner = getSavedAccount();
        Event event1 = getSavedEventMin(owner);
        Event event2 = getSavedEventMin(owner);
        Photo photo0 = getSavedPhoto(event2, owner);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo0)), response.as(IdLists.class).getPhotos());

        Photo photo1 = getSavedPhoto(event1, owner);
        Photo photo2 = getSavedPhoto(event1, owner);
        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(photo1, photo2)), response.as(IdLists.class).getPhotos());
    }

    @Test
    public void testFindPhotosByAccountAndEventSuccess() {
        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();
        Account another = getSavedAccount();
        Event event1 = getSavedEventMin(owner1);
        Event event2 = getSavedEventMin(owner2);
        getSavedAccountEvent(owner2, event1, getRoleMember());
        getSavedAccountEvent(another, event2, getRoleMember());

        Photo photo1 = getSavedPhoto(event1, owner1);
        Photo photo2 = getSavedPhoto(event2, owner2);
        Photo photo3 = getSavedPhoto(event1, owner2);
        Photo photo33 = getSavedPhoto(event1, owner2);
        Photo photo4 = getSavedPhoto(event2, another);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event1.getId(), owner1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo1)), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event1.getId(), owner2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo3, photo33)), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event1.getId(), another.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event2.getId(), owner1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event2.getId(), owner2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo2)), response.as(EntityInfoLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event2.getId(), another.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo4)), response.as(EntityInfoLists.class).getPhotos());
    }

    @Test
    public void testFindPhotoIdsByAccountAndEventSuccess() {
        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();
        Account another = getSavedAccount();
        Event event1 = getSavedEventMin(owner1);
        Event event2 = getSavedEventMin(owner2);
        getSavedAccountEvent(owner2, event1, getRoleMember());
        getSavedAccountEvent(another, event2, getRoleMember());

        Photo photo1 = getSavedPhoto(event1, owner1);
        Photo photo2 = getSavedPhoto(event2, owner2);
        Photo photo3 = getSavedPhoto(event1, owner2);
        Photo photo33 = getSavedPhoto(event1, owner2);
        Photo photo4 = getSavedPhoto(event2, another);

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event1.getId(), owner1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo1)), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event1.getId(), owner2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo3, photo33)), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event1.getId(), another.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event2.getId(), owner1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event2.getId(), owner2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo2)), response.as(IdLists.class).getPhotos());

        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+ACCOUNT_ID, event2.getId(), another.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo4)), response.as(IdLists.class).getPhotos());
    }

    @Test
    public void testFindPhotosByUserAndEventSuccess() {
        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();
        Account another = getSavedAccount();
        Event event1 = getSavedEventMin(owner1);
        Event event2 = getSavedEventMin(owner2);
        getSavedAccountEvent(owner2, event1, getRoleMember());
        getSavedAccountEvent(another, event2, getRoleMember());

        Photo photo1 = getSavedPhoto(event1, owner1);
        Photo photo2 = getSavedPhoto(event2, owner2);
        Photo photo3 = getSavedPhoto(event1, owner2);
        Photo photo33 = getSavedPhoto(event1, owner2);
        Photo photo4 = getSavedPhoto(event2, another);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner1.getId());
        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+SELF, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo1)), response.as(EntityInfoLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner2.getId());
        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+SELF, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo3, photo33)), response.as(EntityInfoLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(another.getId());
        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+SELF, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner1.getId());
        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+SELF, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(EntityInfoLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner2.getId());
        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+SELF, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo2)), response.as(EntityInfoLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(another.getId());
        response = when()
            .get(API+V1+PHOTOS+EVENTS+EVENT_ID+ACCOUNTS+SELF, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(photo4)), response.as(EntityInfoLists.class).getPhotos());
    }

    @Test
    public void testFindPhotoIdsByUserAndEventSuccess() {
        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();
        Account another = getSavedAccount();
        Event event1 = getSavedEventMin(owner1);
        Event event2 = getSavedEventMin(owner2);
        getSavedAccountEvent(owner2, event1, getRoleMember());
        getSavedAccountEvent(another, event2, getRoleMember());

        Photo photo1 = getSavedPhoto(event1, owner1);
        Photo photo2 = getSavedPhoto(event2, owner2);
        Photo photo3 = getSavedPhoto(event1, owner2);
        Photo photo33 = getSavedPhoto(event1, owner2);
        Photo photo4 = getSavedPhoto(event2, another);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner1.getId());
        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo1)), response.as(IdLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner2.getId());
        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo3, photo33)), response.as(IdLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(another.getId());
        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner1.getId());
        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(emptyList(), response.as(IdLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner2.getId());
        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo2)), response.as(IdLists.class).getPhotos());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(another.getId());
        response = when()
            .get(API+V1+PHOTOS+ID+EVENTS+EVENT_ID+ACCOUNTS+SELF, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(photo4)), response.as(IdLists.class).getPhotos());
    }


    //***************************************
    //                 @POST
    //***************************************


    @Test
    public void testAddCommentToPhotoSuccess() {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo photo = getSavedPhoto(event, owner);

        CommentDTO ownerComment = new CommentDTO("some text" + System.currentTimeMillis());
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        Mockito.when(springMethodSecurityService.getUser()).thenReturn(owner);
        MockMvcResponse response = given()
            .body(ownerComment)
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, photo.getId())
        .then()
            .statusCode(SC_CREATED).extract().response();

        Long ownerCommentId = response.as(IdInfo.class).getId();
        List<Comment> comments = this.commentService.getCommentsByPhoto(photo.getId());
        assertEquals(1, comments.size());
        assertTrue(comments.stream().filter(comment -> comment.getText().equals(ownerComment.getCommentText()) &&
                                            comment.getId().equals(ownerCommentId)).findFirst().isPresent());

        CommentDTO memberComment = new CommentDTO("some text" + System.currentTimeMillis());
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(member.getId());
        response = given()
            .body(memberComment)
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, photo.getId())
        .then()
            .statusCode(SC_CREATED).extract().response();

        Long memberCommentId = response.as(IdInfo.class).getId();
        comments = this.commentService.getCommentsByPhoto(photo.getId());
        assertEquals(2, comments.size());
        assertTrue(comments.stream().filter(comment -> comment.getText().equals(ownerComment.getCommentText()) &&
                                            comment.getId().equals(ownerCommentId)).findFirst().isPresent());
        assertTrue(comments.stream().filter(comment -> comment.getText().equals(memberComment.getCommentText()) &&
                                            comment.getId().equals(memberCommentId)).findFirst().isPresent());
    }

    @Test
    public void testAddCommentToPhotoFailureShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo photo = getSavedPhoto(event, owner);

        CommentDTO commentDto = new CommentDTO("some text" + System.currentTimeMillis());

        // without owner ID
        given()
            .body(commentDto)
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, photo.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // with a non-existent user id
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(Long.MAX_VALUE);
        given()
            .body(commentDto)
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, photo.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testAddCommentToEventFailureShouldReturnBadRequest() {
        Account user = getSavedAccount();
        Event event = getSavedEventMax(user);
        Photo photo = getSavedPhoto(event, user);

        // without a request body
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        Mockito.when(springMethodSecurityService.getUser()).thenReturn(user);
        given()
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, event.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);

        // with an empty comment text
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        Mockito.when(springMethodSecurityService.getUser()).thenReturn(user);
        given()
            .body(new CommentDTO(""))
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, event.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);

        // with a comment text is null
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        Mockito.when(springMethodSecurityService.getUser()).thenReturn(user);
        given()
            .body(new CommentDTO())
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, event.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);

        // with a non-existent photo id
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        given()
            .body(new CommentDTO("hi"))
            .contentType(JSON)
        .when()
            .post(API+V1+PHOTOS+PHOTO_ID+COMMENTS, Long.MAX_VALUE)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }


    //***************************************
    //                 @PUT
    //***************************************


    @Test
    public void testUpdatePhotoSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo = getSavedPhoto(event, owner);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        String newPhotoName = photo.getName() + "!";
        MockMvcResponse response = given()
            .param(PHOTO.NAME, newPhotoName)
        .when()
            .put(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        assertEquals(newPhotoName, response.as(PhotoInfo.class).getName());
        assertEquals(newPhotoName, this.photoService.get(photo.getId()).getName());
    }

    @Test
    public void testUpdatePhotoShouldReturnNotFound() {
        given()
            .param(PHOTO.NAME, "super photo")
        .when()
            .put(API+V1+PHOTOS+PHOTO_ID, "1abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        given()
            .param(PHOTO.NAME, "super photo")
        .when()
            .put(API+V1+PHOTOS+PHOTO_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testUpdatePhotoFailureShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo photo = getSavedPhoto(event, owner);

        // the user does not exist or the user doesn't belong to the event
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(Long.MAX_VALUE);
        given()
            .param(PHOTO.NAME, "super photo")
        .when()
            .put(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // no permission
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(member.getId());
        given()
            .param(PHOTO.NAME, "super photo")
        .when()
            .put(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testUpdatePhotoFailureShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo = getSavedPhoto(event, owner);

        // without a photo name
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        when()
            .put(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);
    }


    //***************************************
    //               @DELETE
    //***************************************


    @Test
    public void testRemovePhotoByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Photo photo = getSavedPhoto(event, owner);
        assertNotNull(this.photoService.get(photo.getId()));

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        when()
            .delete(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
        assertNull(this.photoService.get(photo.getId()));

        // and the following queries should return NO CONTENT
        when()
            .delete(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
        assertNull(this.photoService.get(photo.getId()));
    }

    @Test
    public void testRemovePhotoByIdFailureShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo photo = getSavedPhoto(event, owner);

        // the user does not exist or the user doesn't belong to the event
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(Long.MAX_VALUE);
        when()
            .delete(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // no permission
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(member.getId());
        when()
            .delete(API+V1+PHOTOS+PHOTO_ID, photo.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }


    //***************************************
    //                PRIVATE
    //***************************************


    private PhotoInfo convert(final Photo photo) {
        return new PhotoInfo(photo);
    }

    private List<PhotoInfo> convert(final Stream<Photo> photoStream) {
        return photoStream.unordered().map(this::convert).collect(toList());
    }

    private PhotoInfo convert(final Photo photo, final String fields) {
        return PhotoInfo.Transformer.transform(photo, fields);
    }

    private List<PhotoInfo> convert(final Stream<Photo> photoStream, String fields) {
        return photoStream.unordered().map(event -> convert(event, fields)).collect(toList());
    }

}