package com.bionic.fp.web.rest.v1;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.service.impl.SpringMethodSecurityService;
import com.bionic.fp.web.rest.dto.CommentDTO;
import com.bionic.fp.web.rest.dto.CommentInfo;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.rest.dto.IdLists;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link CommentController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-spring-security.xml"})
public class CommentRestControllerTest extends AbstractIT {

    @Autowired SpringMethodSecurityService springMethodSecurityService;
    @Autowired CommentController commentController;

    @Override
    @Before
    public void setUp() {
        springMethodSecurityService = Mockito.mock(SpringMethodSecurityService.class);
        ReflectionTestUtils.setField(springMethodSecurityService, "roleService", roleService);
        ReflectionTestUtils.setField(springMethodSecurityService, "accountService", accountService);
        ReflectionTestUtils.setField(commentController, "methodSecurityService", springMethodSecurityService);
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());
    }


    //***************************************
    //                 @GET
    //***************************************


    @Test
    public void testFindCommentsByIdSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Comment comment = getSavedComment(event, owner);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        CommentInfo accountInfo = response.as(CommentInfo.class);

        assertEquals(convert(comment, event), accountInfo);
    }

    @Test
    public void testFindCommentsByIdShouldReturnNotFound() throws Exception {
        when()
            .get(API+V1+COMMENTS+COMMENT_ID, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        when()
            .get(API+V1+COMMENTS+COMMENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindCommentsByAccountSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo ownerPhoto = getSavedPhoto(event, owner);
        Photo memberPhoto = getSavedPhoto(event, member);

        Comment memberComment1 = getSavedComment(event, member);
        Comment memberComment2 = getSavedComment(memberPhoto, member);
        Comment memberComment3 = getSavedComment(ownerPhoto, member);
        Comment ownerComment1 = getSavedComment(memberPhoto, owner);
        Comment ownerComment2 = getSavedComment(ownerPhoto, owner);
        Comment ownerComment3 = getSavedComment(event, owner);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        List<CommentInfo> comments = of(
                convert(ownerComment1, memberPhoto),
                convert(ownerComment2, ownerPhoto),
                convert(ownerComment3, event)).collect(toList());
        assertEqualsIgnoreOrder(comments, response.as(EntityInfoLists.class).getComments());

        response = when()
            .get(API+V1+COMMENTS+ACCOUNTS+ACCOUNT_ID, member.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        comments = of(
                convert(memberComment1, event),
                convert(memberComment2, memberPhoto),
                convert(memberComment3, ownerPhoto)).collect(toList());
        assertEqualsIgnoreOrder(comments, response.as(EntityInfoLists.class).getComments());
    }

    @Test
    public void testFindCommentIdsByAccountSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo ownerPhoto = getSavedPhoto(event, owner);
        Photo memberPhoto = getSavedPhoto(event, member);

        Comment memberComment1 = getSavedComment(event, member);
        Comment memberComment2 = getSavedComment(memberPhoto, member);
        Comment memberComment3 = getSavedComment(ownerPhoto, member);
        Comment ownerComment1 = getSavedComment(memberPhoto, owner);
        Comment ownerComment2 = getSavedComment(ownerPhoto, owner);
        Comment ownerComment3 = getSavedComment(event, owner);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+ID+ACCOUNTS+ACCOUNT_ID, owner.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(ownerComment1, ownerComment2, ownerComment3)), response.as(IdLists.class).getComments());

        response = when()
            .get(API+V1+COMMENTS+ID+ACCOUNTS+ACCOUNT_ID, member.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(memberComment1, memberComment2, memberComment3)), response.as(IdLists.class).getComments());
    }

    @Test
    public void testFindCommentsByUserSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo ownerPhoto = getSavedPhoto(event, owner);
        Photo memberPhoto = getSavedPhoto(event, member);

        Comment memberComment1 = getSavedComment(event, member);
        Comment memberComment2 = getSavedComment(memberPhoto, member);
        Comment memberComment3 = getSavedComment(ownerPhoto, member);
        Comment ownerComment1 = getSavedComment(memberPhoto, owner);
        Comment ownerComment2 = getSavedComment(ownerPhoto, owner);
        Comment ownerComment3 = getSavedComment(event, owner);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        List<CommentInfo> comments = of(
                convert(ownerComment1, memberPhoto),
                convert(ownerComment2, ownerPhoto),
                convert(ownerComment3, event)).collect(toList());
        assertEqualsIgnoreOrder(comments, response.as(EntityInfoLists.class).getComments());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(member.getId());
        response = when()
            .get(API+V1+COMMENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        comments = of(
                convert(memberComment1, event),
                convert(memberComment2, memberPhoto),
                convert(memberComment3, ownerPhoto)).collect(toList());
        assertEqualsIgnoreOrder(comments, response.as(EntityInfoLists.class).getComments());
    }

    @Test
    public void testFindCommentIdsByUserSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo ownerPhoto = getSavedPhoto(event, owner);
        Photo memberPhoto = getSavedPhoto(event, member);

        Comment memberComment1 = getSavedComment(event, member);
        Comment memberComment2 = getSavedComment(memberPhoto, member);
        Comment memberComment3 = getSavedComment(ownerPhoto, member);
        Comment ownerComment1 = getSavedComment(memberPhoto, owner);
        Comment ownerComment2 = getSavedComment(ownerPhoto, owner);
        Comment ownerComment3 = getSavedComment(event, owner);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+ID+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(ownerComment1, ownerComment2, ownerComment3)), response.as(IdLists.class).getComments());

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(member.getId());
        response = when()
            .get(API+V1+COMMENTS+ID+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(memberComment1, memberComment2, memberComment3)), response.as(IdLists.class).getComments());
    }

    @Test
    public void testFindCommentsByEventSuccess() throws Exception {
        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();
        Event event1 = getSavedEventMin(owner1);
        Event event2 = getSavedEventMin(owner2);
        getSavedAccountEvent(owner2, event1, getRoleMember());

        Comment comment1 = getSavedComment(event1, owner1);
        Comment comment2 = getSavedComment(event2, owner2);
        Comment comment3 = getSavedComment(event1, owner2);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+EVENTS+EVENT_ID, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(convert(of(comment1, comment3), event1), response.as(EntityInfoLists.class).getComments());

        response = when()
            .get(API+V1+COMMENTS+EVENTS+EVENT_ID, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(comment2), event2), response.as(EntityInfoLists.class).getComments());
    }

    @Test
    public void testFindCommentIdsByEventSuccess() throws Exception {
        Account owner1 = getSavedAccount();
        Account owner2 = getSavedAccount();
        Event event1 = getSavedEventMin(owner1);
        Event event2 = getSavedEventMin(owner2);
        getSavedAccountEvent(owner2, event1, getRoleMember());

        Comment comment1 = getSavedComment(event1, owner1);
        Comment comment2 = getSavedComment(event2, owner2);
        Comment comment3 = getSavedComment(event1, owner2);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+ID+EVENTS+EVENT_ID, event1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(comment1, comment3)), response.as(IdLists.class).getComments());

        response = when()
            .get(API+V1+COMMENTS+ID+EVENTS+EVENT_ID, event2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(comment2)), response.as(IdLists.class).getComments());
    }

    @Test
    public void testFindCommentsByPhotoSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo photo1 = getSavedPhoto(event, owner);
        Photo photo2 = getSavedPhoto(event, member);

        Comment comment1 = getSavedComment(photo1, owner);
        Comment comment2 = getSavedComment(photo2, owner);
        Comment comment3 = getSavedComment(photo1, member);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+PHOTOS+PHOTO_ID, photo1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(convert(of(comment1, comment3), photo1), response.as(EntityInfoLists.class).getComments());

        response = when()
            .get(API+V1+COMMENTS+PHOTOS+PHOTO_ID, photo2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(convert(of(comment2), photo2), response.as(EntityInfoLists.class).getComments());
    }

    @Test
    public void testFindCommentIdsByPhotoSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Photo photo1 = getSavedPhoto(event, owner);
        Photo photo2 = getSavedPhoto(event, member);

        Comment comment1 = getSavedComment(photo1, owner);
        Comment comment2 = getSavedComment(photo2, owner);
        Comment comment3 = getSavedComment(photo1, member);

        MockMvcResponse response = when()
            .get(API+V1+COMMENTS+ID+PHOTOS+PHOTO_ID, photo1.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(comment1, comment3)), response.as(IdLists.class).getComments());

        response = when()
            .get(API+V1+COMMENTS+ID+PHOTOS+PHOTO_ID, photo2.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEquals(mapToId(of(comment2)), response.as(IdLists.class).getComments());
    }


    //***************************************
    //                 @PUT
    //***************************************


    @Test
    public void testUpdateCommentSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Comment comment = getSavedComment(event, owner);

        String newCommentText = comment.getText() + "!";
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        given()
            .body(new CommentDTO(newCommentText))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_OK);
        assertEquals(newCommentText, this.commentService.get(comment.getId()).getText());

        given()
            .body(new CommentDTO(""))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_OK);

        assertTrue(this.commentService.get(comment.getId()).getText().isEmpty());
    }

    @Test
    public void testUpdateCommentShouldReturnNotFound() {
        given()
            .body(new CommentDTO("Some text"))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        given()
            .body(new CommentDTO("Some text"))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testUpdateCommentFailureShouldReturnForbidden() {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);
        getSavedAccountEvent(member, event, getRoleMember());
        Comment comment = getSavedComment(event, owner);

        // the user does not exist or the user doesn't belong to the event
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(Long.MAX_VALUE);
        given()
            .body(new CommentDTO(comment.getText() + "!"))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_FORBIDDEN);

        // no permission (only comment owner)
        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(member.getId());
        given()
            .body(new CommentDTO(comment.getText() + "!"))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void testUpdateCommentFailureShouldReturnBadRequest() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Comment comment = getSavedComment(event, owner);

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        given()
            .body(new CommentDTO(null))
            .contentType(JSON)
        .when()
            .put(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_BAD_REQUEST);
    }


    //***************************************
    //               @DELETE
    //***************************************


    @Test
    public void testRemoveCommentByIdSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Comment comment = getSavedComment(event, owner);
        assertNotNull(this.commentService.get(comment.getId()));

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(owner.getId());
        when()
            .delete(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
        assertNull(this.commentService.get(comment.getId()));

        // and the following queries should return NO CONTENT
        when()
            .delete(API+V1+COMMENTS+COMMENT_ID, comment.getId())
        .then()
            .statusCode(SC_NO_CONTENT);
        assertNull(this.commentService.get(comment.getId()));
    }


    //***************************************
    //                PRIVATE
    //***************************************


    private CommentInfo convert(final Comment comment) {
        CommentInfo result = new CommentInfo(comment);
        if(result.getDate() != null) {
            result.setDate(LocalDateTime.parse(result.getDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        return result;
    }

    private CommentInfo convert(final Comment comment, Photo photo) {
        CommentInfo result = new CommentInfo(comment);
        if(result.getDate() != null) {
            result.setDate(LocalDateTime.parse(result.getDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        result.setPhotoId(photo.getId());
        return result;
    }

    private CommentInfo convert(final Comment comment, Event event) {
        CommentInfo result = new CommentInfo(comment);
        if(result.getDate() != null) {
            result.setDate(LocalDateTime.parse(result.getDate().format(LOCAL_DATE_TIME), LOCAL_DATE_TIME));
        }
        result.setEventId(event.getId());
        return result;
    }

    private List<CommentInfo> convert(final Stream<Comment> commentStream) {
        return commentStream.unordered().map(this::convert).collect(toList());
    }

    private List<CommentInfo> convert(final Stream<Comment> commentStream, final Photo photo) {
        return commentStream.unordered().map(comment -> convert(comment, photo)).collect(toList());
    }

    private List<CommentInfo> convert(final Stream<Comment> commentStream, final Event event) {
        return commentStream.unordered().map(comment -> convert(comment, event)).collect(toList());
    }
}