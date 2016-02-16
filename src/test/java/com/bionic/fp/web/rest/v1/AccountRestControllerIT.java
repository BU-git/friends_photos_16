package com.bionic.fp.web.rest.v1;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.service.impl.SpringMethodSecurityService;
import com.bionic.fp.web.rest.dto.AccountInfo;
import com.bionic.fp.web.rest.dto.EntityInfoLists;
import com.bionic.fp.web.rest.dto.IdInfo;
import com.bionic.fp.web.rest.dto.IdLists;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Stream;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

/**
 * This is an integration test that verifies {@link AccountController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-spring-security.xml"})
public class AccountRestControllerIT extends AbstractIT {

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
    public void testFindAccountByIdSuccess() throws Exception {
        Account account = getSavedAccount();

        MockMvcResponse response = when()
            .get(API+V1+ACCOUNTS+ACCOUNT_ID, account.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        AccountInfo accountInfo = response.as(AccountInfo.class);

        assertEquals(convert(account), accountInfo);
    }

    @Test
    public void testFindAccountByIdShouldReturnNotFound() throws Exception {
        when()
            .get(API+V1+ACCOUNTS+ACCOUNT_ID, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        when()
            .get(API+V1+ACCOUNTS+ACCOUNT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindUserSuccess() throws Exception {
        Account user = getSavedAccount();

        Mockito.when(springMethodSecurityService.getUser()).thenReturn(user);
        MockMvcResponse response = when()
            .get(API+V1+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_OK).extract().response();

        AccountInfo accountInfo = response.as(AccountInfo.class);

        assertEquals(convertUser(user), accountInfo);
    }

    @Test
    public void testFindAccountsByEventSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = when()
            .get(API+V1+ACCOUNTS+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(convert(of(owner)), response.as(EntityInfoLists.class).getAccounts());

        getSavedAccountEvent(member, event, getRoleMember());

        response = when()
            .get(API+V1+ACCOUNTS+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        assertEqualsIgnoreOrder(convert(of(owner, member)), response.as(EntityInfoLists.class).getAccounts());
    }

    @Test
    public void testFindAccountIdsByEventSuccess() throws Exception {
        Account owner = getSavedAccount();
        Account member = getSavedAccount();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = when()
            .get(API+V1+ACCOUNTS+ID+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();
        assertEqualsIgnoreOrder(mapToId(of(owner)), response.as(IdLists.class).getAccounts());

        getSavedAccountEvent(member, event, getRoleMember());

        response = when()
            .get(API+V1+ACCOUNTS+ID+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        assertEqualsIgnoreOrder(mapToId(of(owner, member)), response.as(IdLists.class).getAccounts());
    }

    @Test
    public void testFindEventOwnerSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = when()
            .get(API+V1+ACCOUNTS+OWNER+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        AccountInfo actual = response.as(AccountInfo.class);

        assertEquals(convert(owner), actual);
    }

    @Test
    public void testFindEventOwnerShouldReturnNotFound() throws Exception {
        when()
            .get(API+V1+ACCOUNTS+OWNER+EVENTS+EVENT_ID, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        when()
            .get(API+V1+ACCOUNTS+OWNER+EVENTS+EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void testFindEventOwnerIdSuccess() throws Exception {
        Account owner = getSavedAccount();
        Event event = getSavedEventMin(owner);

        MockMvcResponse response = when()
            .get(API+V1+ACCOUNTS+ID+OWNER+EVENTS+EVENT_ID, event.getId())
        .then()
            .statusCode(SC_OK).extract().response();

        assertEquals(owner.getId(), response.as(IdInfo.class).getId());
    }

    @Test
    public void testFindEventOwnerIdShouldReturnNotFound() throws Exception {
        when()
            .get(API+V1+ACCOUNTS+ID+OWNER+EVENTS+EVENT_ID, "abc")
        .then()
            .statusCode(SC_NOT_FOUND);

        when()
            .get(API+V1+ACCOUNTS+ID+OWNER+EVENTS+EVENT_ID, Long.MAX_VALUE)
        .then()
            .statusCode(SC_NOT_FOUND);
    }


    //***************************************
    //                 @DELETE
    //***************************************


    @Test
    public void testSoftDeleteUserSuccess() throws Exception {
        Account user = getSavedAccount();

        assertNotNull(this.accountService.get(user.getId()));

        Mockito.when(springMethodSecurityService.getUserId()).thenReturn(user.getId());
        when()
            .delete(API+V1+ACCOUNTS+SELF)
        .then()
            .statusCode(SC_NO_CONTENT);

        assertNull(this.accountService.get(user.getId()));
    }


    //***************************************
    //                PRIVATE
    //***************************************


    private AccountInfo convert(final Account account) {
        return new AccountInfo(account);
    }

    private AccountInfo convertUser(final Account account) {
        AccountInfo accountInfo = convert(account);
        accountInfo.setEmail(account.getEmail());
        return accountInfo;
    }

    private List<AccountInfo> convert(final Stream<Account> accountStream) {
        return accountStream.unordered().map(this::convert).collect(toList());
    }

    private List<AccountInfo> convertUser(final Stream<Account> accountStream) {
        return accountStream.unordered().map(this::convertUser).collect(toList());
    }

}