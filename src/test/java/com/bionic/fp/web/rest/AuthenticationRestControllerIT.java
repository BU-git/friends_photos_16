//package com.bionic.fp.web.rest;
//
//import com.bionic.fp.AbstractIT;
//import com.bionic.fp.domain.Account;
//import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
//import com.bionic.fp.web.rest.dto.AuthenticationRequest;
//import com.bionic.fp.web.rest.dto.AuthenticationResponse;
//import com.bionic.fp.web.rest.dto.ErrorInfo;
//import com.bionic.fp.web.security.spring.infrastructure.utils.TokenUtils;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//
//import static com.bionic.fp.Constants.RestConstants.PATH.*;
//import static com.jayway.restassured.http.ContentType.JSON;
//import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static org.apache.http.HttpStatus.*;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
///**
// * This is an integration test that verifies {@link AuthenticationController}
// *
// * @author Sergiy Gabriel
// */
//@ContextConfiguration(value = {
//        "classpath:spring/test-root-context.xml",
//        "classpath:spring/test-stateless-cookie-spring-security.xml"})
//public class AuthenticationRestControllerIT extends AbstractIT {
//
//    @Autowired private TokenUtils tokenUtils;
//
//    @Test
//    public void testAuthenticationByEmailSuccess() {
//        Account account = getNewEmailAccount();
//        save(account);
//
//        AuthenticationRequest authRequest = new AuthenticationRequest(account.getEmail(), account.getPassword());
//
//        AuthenticationResponse authResponse = given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_OK)
//        .extract()
//            .as(AuthenticationResponse.class);
//
//        assertNotNull(authResponse.getToken());
//        assertEquals(this.tokenUtils.getUserEmail(authResponse.getToken()), account.getEmail());
//    }
//
//    @Test
//    public void testAuthenticationByEmailShouldReturnBadRequest() {
//        Account account = getNewEmailAccount();
//        save(account);
//
//        // the both arguments are null
//        AuthenticationRequest authRequest = new AuthenticationRequest(null, null);
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the password is null
//        authRequest = new AuthenticationRequest(account.getEmail(), null);
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the email is null
//        authRequest = new AuthenticationRequest(null, account.getPassword());
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the wrong email
//        authRequest = new AuthenticationRequest(account.getEmail() + "!", account.getPassword());
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the wrong password
//        authRequest = new AuthenticationRequest(account.getEmail(), account.getPassword() + "!");
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the wrong email and password
//        authRequest = new AuthenticationRequest(account.getEmail() + "!", account.getPassword() + "!");
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//    }
//
//    @Test
//    public void testAuthenticationViaRegisterByEmailSuccess() {
//        Account account = getNewEmailAccount();
//        AuthenticationRequest authRequest = new AuthenticationRequest(account.getEmail(), account.getPassword());
//
//        AuthenticationResponse authResponse = given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH + REGISTER)
//        .then()
//            .statusCode(SC_CREATED)
//        .extract()
//            .as(AuthenticationResponse.class);
//
//        assertNotNull(authResponse.getToken());
//        Account actual = this.accountService.getByEmail(account.getEmail());
//        assertNotNull(actual);
//        assertEquals(this.tokenUtils.getUserEmail(authResponse.getToken()), account.getEmail());
//    }
//
//    @Test
//    public void testAuthenticationViaRegisterByEmailShouldReturnBadRequest() {
//        Account account = getNewEmailAccount();
//
//        // the both arguments are null
//        AuthenticationRequest authRequest = new AuthenticationRequest(null, null);
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH + REGISTER)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the password is null
//        authRequest = new AuthenticationRequest(account.getEmail(), null);
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH + REGISTER)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the email is null
//        authRequest = new AuthenticationRequest(null, account.getPassword());
//        given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH + REGISTER)
//        .then()
//            .statusCode(SC_BAD_REQUEST);
//
//        // the both arguments are empty
//        authRequest = new AuthenticationRequest("", "");
//        given()
//                .body(authRequest)
//                .contentType(JSON)
//                .when()
//                .post(API + AUTH + REGISTER)
//                .then()
//                .statusCode(SC_BAD_REQUEST);
//
//        // the password is empty
//        authRequest = new AuthenticationRequest(account.getEmail(), "");
//        given()
//                .body(authRequest)
//                .contentType(JSON)
//                .when()
//                .post(API + AUTH + REGISTER)
//                .then()
//                .statusCode(SC_BAD_REQUEST);
//
//        // the email is empty
//        authRequest = new AuthenticationRequest("", account.getPassword());
//        given()
//                .body(authRequest)
//                .contentType(JSON)
//                .when()
//                .post(API + AUTH + REGISTER)
//                .then()
//                .statusCode(SC_BAD_REQUEST);
//    }
//
//    @Test
//    public void testAuthenticationViaRegisterByEmailEmailAlreadyExistsFailure() {
//        Account account = getSavedAccount();
//        AuthenticationRequest authRequest = new AuthenticationRequest(account.getEmail(), "the same");
//
//        ErrorInfo errorInfo = given()
//            .body(authRequest)
//            .contentType(JSON)
//        .when()
//            .post(API + AUTH + REGISTER)
//        .then()
//            .statusCode(SC_BAD_REQUEST)
//        .extract()
//            .as(ErrorInfo.class);
//
//        assertEquals(errorInfo.getError(), new EmailAlreadyExistException(account.getEmail()).getMessage());
//    }
//
//}