package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.service.impl.SpringMethodSecurityService;
import com.bionic.fp.web.rest.dto.PhotoInfo;
import com.bionic.fp.web.rest.v1.EventController;
import com.bionic.fp.web.rest.v1.PhotoController;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

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
    @Autowired EventController eventController;

    @Override
    @Before
    public void setUp() {
        springMethodSecurityService = Mockito.mock(SpringMethodSecurityService.class);
        ReflectionTestUtils.setField(springMethodSecurityService, "roleService", roleService);
        ReflectionTestUtils.setField(springMethodSecurityService, "accountService", accountService);
        ReflectionTestUtils.setField(eventController, "methodSecurityService", springMethodSecurityService);
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

        PhotoInfo photoInfo = response.as(PhotoInfo.class);
        assertEquals(convert(photo), photoInfo);
    }

    @Test
    public void testFindEventByIdShouldReturnNotFound() {
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
        Photo photo = getSavedPhoto(event, owner, getFileFromResources(TEST_IMAGE_FILE_NAME));

        MockMvcResponse response = when()
            .get(API+V1+PHOTOS+PHOTO_ID+FILE, photo.getId())
        .then()
            .statusCode(SC_OK).extract().response();

//        FileSystemResource fileSystemResource = response.as(FileSystemResource.class);
//        System.out.println(fileSystemResource);
//        assertEquals(photo.getUrl(), fileSystemResource.getURL().toString());
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