package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.rest.json.FBUserInfoResponse;
import com.bionic.fp.rest.json.AuthResponse;
import com.bionic.fp.rest.json.FBUserTokenInfo;
import com.bionic.fp.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
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
                                         @RequestParam(name = "fbToken") String token) {

        AuthResponse authResponse = new AuthResponse();

        FBUserTokenInfo fbUserTokenInfo =
                restTemplate.getForObject(String.format(DEBUG_TOKEN_URL, token, appKey, appSecret), FBUserTokenInfo.class);

        if (fbUserTokenInfo == null) {
            authResponse.setCode(AuthResponse.SERVER_PROBLEM);
            authResponse.setMessage("Unable to verify token.");
            return authResponse;
        }

        if (fbUserTokenInfo.hasError()) {
            authResponse.setCode(AuthResponse.BAD_FB_TOKEN);
            authResponse.setMessage(fbUserTokenInfo.getData().getError().getMessage());
            return authResponse;
        }

        FBUserInfoResponse userInfo
                = restTemplate.getForObject(String.format(REQUEST_USER_INFO_URL, fbId, token), FBUserInfoResponse.class);

        if (userInfo.hasError()) {
            authResponse.setCode(AuthResponse.BAD_FB_TOKEN);
            authResponse.setMessage(userInfo.getError().getMessage());
            return authResponse;
        }

        Account account = accountService.getOrCreateAccountForFBId(userInfo.getId(),
                userInfo.getName(), userInfo.getEmail());

        authResponse.setCode(AuthResponse.AUTHENTICATED);
        authResponse.setUserId(String.valueOf(account.getId()));
        authResponse.setToken("");//TODO: set valid access token here

        return authResponse;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
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
