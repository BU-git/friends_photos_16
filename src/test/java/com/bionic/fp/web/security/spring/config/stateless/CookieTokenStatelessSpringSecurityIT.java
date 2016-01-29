package com.bionic.fp.web.security.spring.config.stateless;

import com.bionic.fp.domain.Account;
import com.bionic.fp.web.rest.dto.AuthenticationRequest;
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
import javax.servlet.http.Cookie;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateless-cookie-spring-security.xml"})
public class CookieTokenStatelessSpringSecurityIT extends AbstractAuthenticationFailureIT {

    @Resource private FilterChainProxy springSecurityFilterChain;
    @Resource private TokenUtils tokenUtils;
    @Value("${token.cookie}") private String cookieToken;

    @Override
    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build());
    }

    @Test
    public void testAuthenticationByEmailSuccess() {
        Account account = getNewEmailAccount();
        save(account);

        AuthenticationRequest authRequest = new AuthenticationRequest(account.getEmail(), account.getPassword());

        MockMvcResponse response = given()
            .body(authRequest)
            .contentType(JSON)
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        Cookie cookie = response.mockHttpServletResponse().getCookie(cookieToken);
        assertNotNull(cookie);

        String token = cookie.getValue();
        assertNotNull(token);
        assertEquals(this.tokenUtils.getUserEmail(token), account.getEmail());

        response = given()
            .contentType(JSON)
            .cookie(transform(cookie))
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertNull(response.mockHttpServletResponse().getCookie(cookieToken));

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists);
        assertNotNull(lists.getEvents());
        assertTrue(lists.getEvents().isEmpty());
    }

    @Test
    public void testAuthenticationViaRegisterByEmailSuccess() {
        Account account = getNewEmailAccount();
        AuthenticationRequest authRequest = new AuthenticationRequest(
                account.getEmail(), account.getPassword(), account.getUserName());

        MockMvcResponse response = given()
            .body(authRequest)
            .contentType(JSON)
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_CREATED)
        .extract().response();

        Cookie cookie = response.mockHttpServletResponse().getCookie(cookieToken);
        assertNotNull(cookie);
        String token = cookie.getValue();
        assertNotNull(token);
        assertNotNull(this.accountService.getByEmail(account.getEmail()));
        assertEquals(this.tokenUtils.getUserEmail(token), account.getEmail());

        response = given()
            .contentType(JSON)
            .cookie(transform(cookie))
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertNull(response.mockHttpServletResponse().getCookie(cookieToken));

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
            .post(API + V1 + AUTH + FB)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        Cookie cookie = response.mockHttpServletResponse().getCookie(cookieToken);
        assertNotNull(cookie);
        String token = cookie.getValue();
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
            .cookie(transform(cookie))
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertNull(response.mockHttpServletResponse().getCookie(cookieToken));

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
            .post(API + V1 + AUTH + FB)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        Cookie cookie = response.mockHttpServletResponse().getCookie(cookieToken);
        assertNotNull(cookie);
        String token = cookie.getValue();
        assertNotNull(token);

        response = given()
            .contentType(JSON)
            .cookie(transform(cookie))
        .when()
            .get(API+V1+EVENTS+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK)
        .extract().response();

        assertNull(response.mockHttpServletResponse().getCookie(cookieToken));

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists);
        assertNotNull(lists.getEvents());
        assertTrue(lists.getEvents().isEmpty());
    }
}