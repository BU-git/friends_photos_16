package com.bionic.fp.web.security.spring.config.stateless;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.web.rest.dto.AuthenticationResponse;
import com.bionic.fp.web.rest.dto.AuthenticationSocialRequest;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.security.spring.infrastructure.utils.TokenUtils;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateless-header-spring-security.xml"})
public class HeaderTokenStatelessSpringSecurityIT extends AbstractAuthenticationFailureIT {

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
    public void testAuthenticationByEmailSuccess() {
        Account account = getNewEmailAccount();
        Account actual = save(account);

        MockMvcResponse response = given()
            .param(ACCOUNT.EMAIL, account.getEmail())
            .param(ACCOUNT.PASSWORD, account.getPassword())
        .when()
            .post(API+V1+AUTH)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        AuthenticationResponse authResponse = response.as(AuthenticationResponse.class);

        assertEquals(actual.getId(), authResponse.getUserId());
        assertEquals(actual.getEmail(), authResponse.getEmail());

        String token = authResponse.getToken();
        assertNotNull(token);
        assertEquals(this.tokenUtils.getUserEmail(token), account.getEmail());

        response = given()
            .contentType(JSON)
            .header(tokenHeader, token)
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists);
        assertNotNull(lists.getEvents());
        assertTrue(lists.getEvents().isEmpty());
    }

    @Test
    public void testAuthenticationViaRegisterByEmailSuccess() {
        Account account = getNewEmailAccount();

        MockMvcResponse response = given()
            .param(ACCOUNT.EMAIL, account.getEmail())
            .param(ACCOUNT.PASSWORD, account.getPassword())
            .param(ACCOUNT.USERNAME, account.getUserName())
        .when()
            .post(API+V1+AUTH+REGISTER)
        .then()
            .statusCode(SC_CREATED)
        .extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        AuthenticationResponse authResponse = response.as(AuthenticationResponse.class);

        assertNotNull(authResponse.getUserId());
        assertEquals(account.getEmail(), authResponse.getEmail());

        String token = authResponse.getToken();
        assertNotNull(token);
        assertNotNull(this.accountService.getByEmail(account.getEmail()));
        assertEquals(this.tokenUtils.getUserEmail(token), account.getEmail());

        response = given()
            .contentType(JSON)
            .header(tokenHeader, token)
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists);
        assertNotNull(lists.getEvents());
        assertTrue(lists.getEvents().isEmpty());
    }

    @Test
    public void testAuthenticationViaRegisterByFacebookSuccess() {
        AuthenticationSocialRequest socialRequest = new AuthenticationSocialRequest();

        socialRequest.setSocialId("fb" + System.currentTimeMillis());
        socialRequest.setToken("some token");
        socialRequest.setEmail(generateEmail());
        socialRequest.setUsername(generateUsername());
        socialRequest.setFirstName("First");
        socialRequest.setLastName("Last");
        socialRequest.setImage("http://image.jpg");

        MockMvcResponse response = given()
            .body(socialRequest)
            .contentType(JSON)
        .when()
            .post(API+V1+AUTH+FB)
        .then()
            .statusCode(SC_OK).extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        AuthenticationResponse authResponse = response.as(AuthenticationResponse.class);

        assertNotNull(authResponse.getUserId());
        assertEquals(socialRequest.getEmail(), authResponse.getEmail());

        String token = authResponse.getToken();
        assertNotNull(token);
        assertEquals(this.tokenUtils.getUserEmail(token), socialRequest.getEmail());
        Account actual = this.accountService.get(this.tokenUtils.getUserId(token));
        assertNotNull(actual);
        assertEquals(socialRequest.getSocialId(), actual.getFbId());
//        assertEquals(socialRequest.getToken(), actual.getFbToken());
        assertEquals(socialRequest.getEmail(), actual.getEmail());
        assertEquals(socialRequest.getUsername(), actual.getUserName());
        assertEquals(socialRequest.getImage(), actual.getProfileImageUrl());

        response = given()
            .contentType(JSON)
            .header(tokenHeader, token)
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists);
        assertNotNull(lists.getEvents());
        assertTrue(lists.getEvents().isEmpty());
    }

    @Test
    public void testAuthenticationByFacebookSuccess() {
        AuthenticationSocialRequest socialRequest = new AuthenticationSocialRequest();
        socialRequest.setSocialId("fb" + System.currentTimeMillis());
        socialRequest.setToken("some token");
        socialRequest.setEmail(generateEmail());
        socialRequest.setUsername(generateUsername());
        socialRequest.setFirstName("First");
        socialRequest.setLastName("Last");
        socialRequest.setImage("http://image.jpg");

        Account account = this.accountService.getOrCreateFbAccount(socialRequest);
        assertNotNull(account);
        assertNotNull(account.getId());

        AuthenticationSocialRequest authRequest = new AuthenticationSocialRequest();
        authRequest.setSocialId(socialRequest.getSocialId());

        MockMvcResponse response = given()
            .body(authRequest)
            .contentType(JSON)
        .when()
            .post(API+V1+AUTH+FB)
        .then()
            .statusCode(SC_OK).extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        AuthenticationResponse authResponse = response.as(AuthenticationResponse.class);

        assertEquals(account.getId(), authResponse.getUserId());
        assertEquals(account.getEmail(), authResponse.getEmail());

        String token = authResponse.getToken();
        assertNotNull(token);

        response = given()
            .contentType(JSON)
            .header(tokenHeader, token)
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertTrue(response.mockHttpServletResponse().getCookies().length == 0);
        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists);
        assertNotNull(lists.getEvents());
        assertTrue(lists.getEvents().isEmpty());
    }
}