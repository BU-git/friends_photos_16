package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.Constants;
import com.bionic.fp.Constants.RestConstants.PHOTO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.EventType;
import com.bionic.fp.web.rest.dto.EventInput;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.List;

import static com.bionic.fp.Constants.RestConstants.EVENT;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * This is an integration test that verifies {@link PhotoController}
 *
 * @author Sergiy Gabriel
 */
public class PhotoRestControllerIT extends AbstractIT {

    public static final File file = new File("/Users/segabriel/googleDrive/BearGrylls.JPG");

    @Test
    public void testSaveEventSuccess() {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(owner.getId())).build();

        given()
            .multiPart(file)
            .param(EVENT.ID, event.getId())
            .contentType(MULTIPART_FORM_DATA_VALUE)
        .when()
            .post(PHOTOS)
        .then()
            .statusCode(SC_CREATED);

        given()
            .multiPart(file)
            .param(EVENT.ID, event.getId())
            .param(PHOTO.NAME, "BearGrylls")
            .contentType(MULTIPART_FORM_DATA_VALUE)
        .when()
            .post(PHOTOS)
        .then()
            .statusCode(SC_CREATED)
            .body(PHOTO.NAME + TO_STRING, is("BearGrylls"));
    }

}