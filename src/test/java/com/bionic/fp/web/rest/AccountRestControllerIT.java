package com.bionic.fp.web.rest;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import com.bionic.fp.web.rest.dto.ErrorInfo;
import com.bionic.fp.web.rest.dto.IdInfo;
import com.bionic.fp.web.rest.dto.AccountInput;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This is an integration test that verifies {@link AccountController}
 *
 * @author Sergiy Gabriel
 */
public class AccountRestControllerIT extends AbstractIT {

    @Test
    public void testRegisterAccountSuccess() {
        Account account = getNewEmailAccount();
        AccountInput accountInput = new AccountInput(account.getEmail(), account.getPassword());

        IdInfo idInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + REGISTER)
        .then()
            .statusCode(SC_CREATED)
        .extract()
            .as(IdInfo.class);

        assertNotNull(this.accountService.get(idInfo.getId()));
    }

    @Test
    public void testRegisterAccountEmailAlreadyExistsFailure() {
        Account account = getSavedAccount();
        AccountInput accountInput = new AccountInput(account.getEmail(), account.getPassword());

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), new EmailAlreadyExistException(account.getEmail()).getMessage());
    }

    @Test
    public void testRegisterAccountEmailNullEmptyFailure() {
        AccountInput accountInput = new AccountInput(null, "secret");

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), "The email should not be null/empty");

        accountInput = new AccountInput("", "secret");

        errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), "The email should not be null/empty");
    }

    @Test
    public void testRegisterAccountPasswordNullEmptyFailure() {
        Account account = getNewEmailAccount();
        AccountInput accountInput = new AccountInput(account.getEmail(), null);

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), new IncorrectPasswordException("Password is empty").getMessage());

        accountInput = new AccountInput(account.getEmail(), "");

        errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), new IncorrectPasswordException("Password is empty").getMessage());
    }

    @Test
    public void testLoginAccountSuccess() {
        Account account = getNewEmailAccount();
        Long accountId = save(account);

//        Account account = getSavedAccount();
        AccountInput accountInput = new AccountInput(account.getEmail(), account.getPassword());

        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(getFilter(accountId)).build();

        IdInfo idInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS+LOGIN)
        .then()
            .statusCode(SC_OK)
        .extract()
            .as(IdInfo.class);

        assertEquals(accountId, idInfo.getId());
    }

    @Test
    public void testLoginAccountEmailNullFailure() {
        AccountInput accountInput = new AccountInput(null, "secret");

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + LOGIN)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), "The email should not be null");
    }

    @Test
    public void testLoginAccountPasswordNullFailure() {
        Account account = getNewEmailAccount();
        AccountInput accountInput = new AccountInput(account.getEmail(), null);

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + LOGIN)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), "The password should not be null");
    }

    @Test
    public void testLoginAccountAccountNotFoundFailure() {
        AccountInput accountInput = new AccountInput("impossible", "secret");

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + LOGIN)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), new AccountNotFoundException("impossible").getMessage());
    }

    @Test
    public void testLoginAccountIncorrectPasswordFailure() {
        Account account = getSavedAccount();
        AccountInput accountInput = new AccountInput(account.getEmail(), account.getPassword() + "!");

        ErrorInfo errorInfo = given()
            .body(accountInput)
            .contentType(JSON)
        .when()
            .post(API+ACCOUNTS + LOGIN)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), new IncorrectPasswordException().getMessage());
    }

}