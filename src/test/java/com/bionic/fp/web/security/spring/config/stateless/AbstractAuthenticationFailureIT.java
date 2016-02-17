package com.bionic.fp.web.security.spring.config.stateless;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.Constants;
import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.web.rest.dto.AuthenticationRequest;
import com.bionic.fp.web.rest.dto.ErrorInfo;
import org.junit.Test;

import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public abstract class AbstractAuthenticationFailureIT extends AbstractIT {

    private static final Object NULL = null;

    @Test
    public void testAuthenticationFailureShouldReturnUnauthorized() throws Exception {
        given()
            .contentType(JSON)
        .when()
            .get(API + V1 + "/any")
        .then()
            .statusCode(SC_UNAUTHORIZED);

        given()
            .contentType(JSON)
        .when()
            .get(API + V1 + EVENTS)
        .then()
            .statusCode(SC_UNAUTHORIZED);

        given()
            .contentType(JSON)
        .when()
            .get(API + V1 + ACCOUNTS)
        .then()
            .statusCode(SC_UNAUTHORIZED);

        given()
            .contentType(JSON)
        .when()
            .get(API + V1 + ROLES)
        .then()
            .statusCode(SC_UNAUTHORIZED);

        given()
            .contentType(JSON)
        .when()
            .get(API + V1 + COMMENTS)
        .then()
            .statusCode(SC_UNAUTHORIZED);

        given()
            .contentType(JSON)
        .when()
            .get(API + V1 + PHOTOS)
        .then()
            .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testAuthenticationByEmailShouldReturnBadRequest() {
        Account account = getNewEmailAccount();
        save(account);

        // the both arguments are null
        when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the password is null
        given()
            .param(ACCOUNT.EMAIL, account.getEmail())
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the email is null
        given()
            .param(ACCOUNT.PASSWORD, account.getPassword())
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the both arguments are empty
        given()
            .param(ACCOUNT.EMAIL, "")
            .param(ACCOUNT.PASSWORD, "")
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the password is empty
        given()
            .param(ACCOUNT.EMAIL, account.getEmail())
            .param(ACCOUNT.PASSWORD, "")
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the email is empty
        given()
            .param(ACCOUNT.EMAIL, "")
            .param(ACCOUNT.PASSWORD, account.getPassword())
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the wrong email
        given()
            .param(ACCOUNT.EMAIL, account.getEmail() + "!")
            .param(ACCOUNT.PASSWORD, account.getPassword())
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the wrong password
        given()
            .param(ACCOUNT.EMAIL, account.getEmail())
            .param(ACCOUNT.PASSWORD, account.getPassword() + "!")
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the wrong email and password
        given()
            .param(ACCOUNT.EMAIL, account.getEmail() + "!")
            .param(ACCOUNT.PASSWORD, account.getPassword() + "!")
        .when()
            .post(API + V1 + AUTH)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testAuthenticationViaRegisterByEmailShouldReturnBadRequest() {
        Account account = getNewEmailAccount();

        // the both arguments are null
        when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the password is null
        given()
            .param(ACCOUNT.EMAIL, account.getEmail())
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the email is null
        given()
            .param(ACCOUNT.PASSWORD, account.getPassword())
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the both arguments are empty
        given()
            .param(ACCOUNT.EMAIL, "")
            .param(ACCOUNT.PASSWORD, "")
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the password is empty
        given()
            .param(ACCOUNT.EMAIL, account.getEmail())
            .param(ACCOUNT.PASSWORD, "")
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST);

        // the email is empty
        given()
            .param(ACCOUNT.EMAIL, "")
            .param(ACCOUNT.PASSWORD, account.getPassword())
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void testAuthenticationViaRegisterByEmailEmailAlreadyExistsFailure() {
        Account account = getSavedAccount();

        ErrorInfo errorInfo = given()
            .param(ACCOUNT.EMAIL, account.getEmail())
            .param(ACCOUNT.PASSWORD, "the same")
        .when()
            .post(API + V1 + AUTH + REGISTER)
        .then()
            .statusCode(SC_BAD_REQUEST)
        .extract()
            .as(ErrorInfo.class);

        assertEquals(errorInfo.getError(), new EmailAlreadyExistException(account.getEmail()).getMessage());
    }
}