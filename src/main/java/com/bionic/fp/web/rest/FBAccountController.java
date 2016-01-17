package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.web.rest.dto.FBUserInfoResponse;
import com.bionic.fp.web.rest.dto.AuthResponse;
import com.bionic.fp.web.rest.dto.FBUserTokenInfo;
import com.bionic.fp.web.security.SessionUtils;
import com.bionic.fp.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.bionic.fp.util.Checks.check;
import static com.bionic.fp.Constants.RestConstants.PARAM.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(API+ACCOUNTS+FB)
public class FBAccountController {
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


    //***************************************
    //                 @POST
    //***************************************


    @RequestMapping(method = POST, produces = APPLICATION_JSON_VALUE)
    public AuthResponse loginViaFacebook(@RequestParam(name = FB_ID) final String fbId,
                                         @RequestParam(name = FB_TOKEN) final String token,
                                         final HttpSession session) throws InvalidParameterException {
        check(isNotEmpty(fbId), "FB id is empty");
        check(isNotEmpty(token), "FB token is empty");

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

        Account account = accountService.getOrCreateAccountForFbId(userInfo.getId(),
                userInfo.getName(), userInfo.getEmail());

        authResponse.setCode(AuthResponse.AUTHENTICATED);
        authResponse.setUserId(String.valueOf(account.getId()));
        authResponse.setToken("");//TODO: set valid access token here

        SessionUtils.setUserId(session, account);

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
