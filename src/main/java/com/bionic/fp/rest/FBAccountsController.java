package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.app.logic.InvalidParameterException;
import com.bionic.fp.rest.dto.FBUserInfoResponse;
import com.bionic.fp.rest.dto.AuthResponse;
import com.bionic.fp.rest.dto.FBUserTokenInfo;
import com.bionic.fp.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@RestController
@RequestMapping("/account/fb")
public class FBAccountsController {
    private static final String DEBUG_TOKEN_URL =
            "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s|%s";
    private static final String REQUEST_USER_INFO_URL =
            "https://graph.facebook.com/%s?fields=name,email&access_token=%s";

    @Inject
    private AccountService accountService;

    @Inject
    private RestTemplate restTemplate;

    @Value("${fb.key}")
    private String appKey;

    @Value("${fb.secret}")
    private String appSecret;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse loginViaFacebook(@RequestParam(name = "fbId") String fbId,
                                         @RequestParam(name = "fbToken") String token) throws InvalidParameterException {

        AuthResponse authResponse = new AuthResponse();

        FBUserTokenInfo fbUserTokenInfo = null;
        try {
            fbUserTokenInfo =
                restTemplate.getForObject(String.format(DEBUG_TOKEN_URL, token, appKey, appSecret), FBUserTokenInfo.class);
        } catch (RestClientException ignored) {}

        if (fbUserTokenInfo == null) {
            throw new InvalidParameterException("Unable to verify token.");
        }

        if (fbUserTokenInfo.hasError()) {
            // Bad FB token
            throw new InvalidParameterException(fbUserTokenInfo.getData().getError().getMessage());
        }

        FBUserInfoResponse userInfo = null;
        try {
            userInfo = restTemplate.getForObject(String.format(REQUEST_USER_INFO_URL, fbId, token), FBUserInfoResponse.class);
        } catch (RestClientException ignored) {}

        if (userInfo == null) {
            throw new InvalidParameterException("Unable to get user info from facebook.");
        } else if (userInfo.hasError()) {
            // Bad FB token
            throw new InvalidParameterException(fbUserTokenInfo.getData().getError().getMessage());
        }

        Account account = accountService.getOrCreateAccountForFBId(userInfo.getId(),
                userInfo.getName(), userInfo.getEmail());

        authResponse.setCode(AuthResponse.AUTHENTICATED);
        authResponse.setUserId(String.valueOf(account.getId()));
        authResponse.setToken("");//TODO: set valid access token here

        return authResponse;
    }

    @PostConstruct
    public void configure() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
    }
}
