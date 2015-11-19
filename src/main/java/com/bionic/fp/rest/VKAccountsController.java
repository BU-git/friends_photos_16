package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.rest.json.*;
import com.bionic.fp.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@RestController
@RequestMapping("/account/vk")
public class VKAccountsController {
    private static final String APP_TOKEN_URL =
            "https://oauth.vk.com/access_token?client_id=%s&client_secret=%s&v=5.40&grant_type=client_credentials";
    private static final String CHECK_USER_TOKEN_URL =
            "https://api.vk.com/method/secure.checkToken?v=5.40&token=%s&client_secret=%s&access_token=%s";

    @Inject
    private AccountService accountService;

    @Inject
    private RestTemplate restTemplate;

    @Value("${vk.key}")
    private String appKey;

    @Value("${vk.secret}")
    private String appSecret;

    private String vkAccessToken;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse loginViaFacebook(@RequestParam(name = "vkId") String vkId,
                                         @RequestParam(name = "vkToken") String token) {

        AuthResponse authResponse = new AuthResponse();

        VKCheckTokenResponse checkTokenResponse = null;
        try {
            checkTokenResponse =
                    restTemplate.getForObject(String.format(CHECK_USER_TOKEN_URL, token, appSecret, vkAccessToken), VKCheckTokenResponse.class);
        } catch (RestClientException ignored) {}

        if (checkTokenResponse == null) {
            authResponse.setCode(AuthResponse.SERVER_PROBLEM);
            authResponse.setMessage("Unable to verify token.");
            return authResponse;
        }

        if (checkTokenResponse.hasError()) {
            authResponse.setCode(AuthResponse.BAD_VK_TOKEN);
            authResponse.setMessage(checkTokenResponse.getError().getErrorMsg());
            return authResponse;
        }

        Account account = accountService.getOrCreateAccountForVKId(checkTokenResponse.getResponse().getUserId());

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

        VKAccessTokenResponse accessTokenResponse =
                restTemplate.getForObject(String.format(APP_TOKEN_URL, appKey, appSecret), VKAccessTokenResponse.class);
        if (accessTokenResponse.hasError()) {
            throw new IllegalStateException("App can't run without vk access token");
        } else {
            vkAccessToken = accessTokenResponse.getAccessToken();
        }
    }
}
