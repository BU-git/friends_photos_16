package com.bionic.fp.web.rest.v1;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Comment;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.service.impl.SpringMethodSecurityService;
import com.bionic.fp.web.rest.dto.CommentInfo;
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
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.util.DateTimeFormatterConstants.LOCAL_DATE_TIME;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

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
    @Autowired AccountController accountController;

    @Override
    @Before
    public void setUp() {
        springMethodSecurityService = Mockito.mock(SpringMethodSecurityService.class);
        ReflectionTestUtils.setField(springMethodSecurityService, "roleService", roleService);
        ReflectionTestUtils.setField(springMethodSecurityService, "accountService", accountService);
        ReflectionTestUtils.setField(accountController, "methodSecurityService", springMethodSecurityService);
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context).build());
    }


    //***************************************
    //                 @GET
    //***************************************


    @Test
    public void testFindCommentsByIdSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);
        Comment comment = getSavedEventComment(event, owner);

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
    public void testFindCommentsByEventSuccess() throws Exception {
    }

    @Test
    public void testGetCommentIdsByEventSuccess() throws Exception {

    }

    @Test
    public void testGetCommentsByPhotoSuccess() throws Exception {

    }

    @Test
    public void testGetCommentIdsByPhotoSuccess() throws Exception {

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