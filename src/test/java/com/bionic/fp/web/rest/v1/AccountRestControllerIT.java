package com.bionic.fp.web.rest.v1;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.web.rest.dto.AccountInfo;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.rest.dto.IdInfo;
import com.bionic.fp.web.rest.dto.IdLists;
import com.bionic.fp.web.security.spring.infrastructure.User;
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
import java.util.List;
import java.util.Optional;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.Constants.RoleConstants.MEMBER;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateless-header-spring-security.xml"})
public class AccountRestControllerIT extends AbstractIT {

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
    public void testGetAccountSuccess() throws Exception {
        Account account = getRegularUser();

        MockMvcResponse response = given()
            .header(tokenHeader, getToken(account))
        .when()
            .get(API+V1+ACCOUNTS+ACCOUNT_ID, account.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        AccountInfo accountInfo = response.as(AccountInfo.class);

        assertEqualsAccount(account, accountInfo);
        assertNotNull(account.getEmail());
        assertNull(accountInfo.getEmail());
    }

    @Test
    public void testGetUserSuccess() throws Exception {
        Account user = getRegularUser();

        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .addFilters(getPreAuthFilter(user), springSecurityFilterChain).build());

        MockMvcResponse response = given()
            .header(tokenHeader, getToken(user))
        .when()
            .get(API+V1+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        AccountInfo accountInfo = response.as(AccountInfo.class);

        assertEqualsAccount(user, accountInfo);
        assertEquals(user.getEmail(), accountInfo.getEmail());
    }

    @Test
    public void testGetAccountsByEventSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account account = getRegularUser();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = given()
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+ACCOUNTS+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        EntityInfoLists lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getAccounts());
        assertTrue(lists.getAccounts().size() == 1);
        assertEqualsAccount(owner, lists.getAccounts().get(0));

        this.eventService.addOrUpdateAccountToEvent(account.getId(), event.getId(), MEMBER, null);

        response = given()
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+ACCOUNTS+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        lists = response.as(EntityInfoLists.class);
        assertNotNull(lists.getAccounts());
        assertTrue(lists.getAccounts().size() == 2);
        assertEqualsAccount(lists.getAccounts(), owner, account);
    }

    @Test
    public void testGetAccountIdsByEventSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account account = getRegularUser();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = given()
                .header(tokenHeader, getToken(owner))
                .when()
                .get(API+V1+ACCOUNTS+ID+EVENTS+EVENT_ID, event.getId())
                .then()
                .statusCode(SC_OK).extract().response();

        IdLists lists = response.as(IdLists.class);
        assertNotNull(lists.getAccounts());
        assertTrue(lists.getAccounts().size() == 1);
        assertEquals(owner.getId(), lists.getAccounts().get(0));

        this.eventService.addOrUpdateAccountToEvent(account.getId(), event.getId(), MEMBER, null);

        response = given()
                .header(tokenHeader, getToken(owner))
                .when()
                .get(API+V1+ACCOUNTS+ID+EVENTS+EVENT_ID, event.getId())
                .then()
                .statusCode(SC_OK).extract().response();

        lists = response.as(IdLists.class);
        assertNotNull(lists.getAccounts());
        assertTrue(lists.getAccounts().size() == 2);
        assertEqualsId(lists.getAccounts(), owner, account);
    }

    @Test
    public void testGetEventOwnerSuccess() throws Exception {
        Account owner = getRegularUser();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = given()
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+ACCOUNTS+OWNER+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        AccountInfo actual = response.as(AccountInfo.class);

        assertEqualsAccount(owner, actual);
    }

    @Test
    public void testGetEventOwnerIdSuccess() throws Exception {
        Account owner = getRegularUser();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = given()
            .header(tokenHeader, getToken(owner))
        .when()
            .get(API+V1+ACCOUNTS+ID+OWNER+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        IdInfo actual = response.as(IdInfo.class);

        assertEquals(owner.getId(), actual.getId());
    }

    private void assertEqualsAccount(final Account expected, final AccountInfo actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserName(), actual.getUsername());
        if(expected.getProfileImageUrl() != null) {
            assertEquals(expected.getFbProfileUrl(), actual.getImageUrl());
        }
    }

    private void assertEqualsAccount(final List<AccountInfo> actuals, final Account... accounts) {
        if(accounts == null || accounts.length == 0) {
            return;
        }
        if(actuals == null || actuals.isEmpty() || accounts.length != actuals.size()) {
            fail();
        }
        for (Account account : accounts) {
            Optional<AccountInfo> optional = actuals.stream().parallel()
                    .filter(a -> account.getId().equals(a.getId())).findFirst();
            if(optional.isPresent()) {
                assertEqualsAccount(account, optional.get());
            } else {
                fail();
            }
        }

    }

    private String getToken(final Account account) {
        return this.tokenUtils.generateToken(new User(account));
    }

}