package com.bionic.fp.web.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.web.rest.dto.AuthenticationRequest;
import com.bionic.fp.web.rest.dto.AuthenticationResponse;
import com.bionic.fp.web.rest.dto.FBUserInfoResponse;
import com.bionic.fp.web.rest.dto.FBUserTokenInfo;
import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationStrategy;
import com.bionic.fp.web.security.spring.infrastructure.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bionic.fp.Constants.RestConstants.PARAM.FB_TOKEN;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static com.bionic.fp.util.Checks.check;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * This is REST web-service that handles authentication requests
 *
 * @author Sergiy Gabriel
 */
@RestController
@RequestMapping(API+AUTH)
public class AuthenticationController {

    private static final String DEBUG_TOKEN_URL = "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s|%s";
    private static final String REQUEST_USER_INFO_URL = "https://graph.facebook.com/%s?fields=name,email&access_token=%s";
    private static final String REQUEST_USER_INFO_URL_ME = "https://graph.facebook.com/me?fields=name,email&access_token=%s";

    @Value("${fb.key}")     private String appKey;
    @Value("${fb.secret}")  private String appSecret;

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private TokenUtils tokenUtils;
    @Autowired private AccountService accountService;
    @Autowired private MethodSecurityService methodSecurityService;
    @Autowired private AuthenticationStrategy authenticationStrategy;
    @Autowired private RestTemplate restTemplate;


    //***************************************
    //                 @POST
    //***************************************


    /**
     * Endpoint used to login by email
     */
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public AuthenticationResponse authentication(@RequestBody final AuthenticationRequest authRequest,
                                                 final HttpServletRequest request, final HttpServletResponse response) {
        User user = this.getAuthenticatedUser(authRequest);
        this.authenticationStrategy.saveAuthentication(user, request, response);
        String token = this.tokenUtils.generateToken(user);
        return new AuthenticationResponse(token, user.getId());
    }

    /**
     * Endpoint used to register by email
     */
    @RequestMapping(value = REGISTER, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ResponseBody
    public AuthenticationResponse register(@RequestBody final AuthenticationRequest authRequest,
                                           final HttpServletRequest request, final HttpServletResponse response) {
        this.accountService.registerByFP(authRequest.getEmail(), authRequest.getPassword(), authRequest.getUsername());
        User user = this.getAuthenticatedUser(authRequest);
        this.authenticationStrategy.saveAuthentication(user, request, response);
        String token = this.tokenUtils.generateToken(user);
        return new AuthenticationResponse(token, user.getId());
    }

    /**
     * Endpoint used to authentication by facebook
     */
    @RequestMapping(value = FB, method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public AuthenticationResponse loginViaFacebook(@RequestParam(name = FB_TOKEN, required = true) final String token,
                                           final HttpServletRequest request, final HttpServletResponse response) {
        // verification
        String fbId = this.getUserFbId(token);

        FBUserInfoResponse userInfo = null;
        try {
            userInfo = restTemplate.getForObject(String.format(REQUEST_USER_INFO_URL, fbId, token), FBUserInfoResponse.class);
//            userInfo = restTemplate.getForObject(String.format(REQUEST_USER_INFO_URL_ME, token), FBUserInfoResponse.class);
        } catch (RestClientException ignored) {}

        check(userInfo != null, "Unable to get user info from facebook");
        check(userInfo.hasError(), userInfo.getError().getMessage());

        Account account = accountService.getOrCreateAccountForFbId(userInfo.getId(),
                userInfo.getName(), userInfo.getEmail());

        User user = new User(account);
        this.authenticationStrategy.saveAuthentication(user, request, response);

        return new AuthenticationResponse(this.tokenUtils.generateToken(user), user.getId());
    }

    @RequestMapping(value = LOGOUT, method = POST)
    @ResponseStatus(OK)
    public final void logout(final HttpServletRequest request, final HttpServletResponse response) {
        this.authenticationStrategy.removeAuthentication(request, response);
    }


    //***************************************
    //                 PRIVATE
    //***************************************


    private AuthenticationResponse getAuthenticationResponse(final AuthenticationRequest authRequest) {
        return this.getAuthenticationResponse(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword()));
    }

    private AuthenticationResponse getAuthenticationResponse(final Authentication auth) {
        // Perform the authentication
        Authentication authentication = this.authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate token
        User user = new User(this.methodSecurityService.getUserId(), this.methodSecurityService.getUserEmail());
        String token = this.tokenUtils.generateToken(user);

        return new AuthenticationResponse(token);
    }

    private User getAuthenticatedUser(final AuthenticationRequest authRequest) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        // Perform the authentication
        Authentication authentication = this.authenticationManager.authenticate(auth);
        return (User) authentication.getPrincipal();
    }

    private String getUserFbId(final String token) {
        FBUserTokenInfo tokenInfo = null;
        try {
            tokenInfo = this.restTemplate.getForObject(String.format(DEBUG_TOKEN_URL, token, appKey, appSecret), FBUserTokenInfo.class);
        } catch (RestClientException ignored) {}

        check(tokenInfo != null, "Unable to verify token");
        check(tokenInfo.hasError(), tokenInfo.getData().getError().getMessage());

        return tokenInfo.getData().getUserId();
    }

}
