package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import com.bionic.fp.exception.auth.impl.UserNameAlreadyExistException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import com.bionic.fp.web.rest.dto.AuthenticationSocialRequest;
import com.bionic.fp.web.rest.dto.service.FBUserInfoResponse;
import com.bionic.fp.web.rest.dto.service.FBUserTokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.bionic.fp.util.Checks.*;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by boubdyk on 11.11.2015.
 */
@Service
@Transactional
public class AccountService {

    private static final String FACEBOOK_BASE_URL = "https://www.facebook.com/";
    private static final String VK_BASE_URL = "http://vk.com/";
    private static final String DEBUG_TOKEN_URL = "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s|%s";
    private static final String REQUEST_USER_INFO_URL = "https://graph.facebook.com/%s?fields=name,email&access_token=%s";
    private static final String REQUEST_USER_INFO_URL_ME = "https://graph.facebook.com/me?fields=name,email&access_token=%s";

    @Value("${fb.key}")     private String appKey;
    @Value("${fb.secret}")  private String appSecret;

    @Autowired private AccountDAO accountDAO;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RestTemplate restTemplate;


    public AccountService() {}

    //////////////////////////////////////////////
    //                  CRUD                    //
    //////////////////////////////////////////////

    /**
     * Returns an account by the account ID
     *
     * @param accountId the account ID
     * @return the account and null otherwise
     * @throws InvalidParameterException if the account ID is invalid
     */
    public Account get(final Long accountId) throws InvalidParameterException {
        checkAccount(accountId);
        return this.accountDAO.read(accountId);
    }

    //////////////////////////////////////////////
    //                  Other                   //
    //////////////////////////////////////////////

    /**
     * Used to register user by FP. If user exist method return user id. If user doesn't exist method
     * create new user in DB and return his id
     * @param email the users email
     * @param password the user password
     * @return unique user identifier
     * @throws UserNameAlreadyExistException if user already exist in DB
     * @throws IncorrectPasswordException if user password is incorrect
     */
    public Long registerByFP(final String email, final String password, final String username) throws EmailAlreadyExistException, IncorrectPasswordException {
        checkEmail(email);
        checkPassword(password);
        if(this.accountDAO.getByEmail(email) != null) {
            throw new EmailAlreadyExistException(email);
        }

        Account account = new Account(email, username, this.passwordEncoder.encode(password));
        this.accountDAO.create(account);
        return account.getId();
    }

//    /**
//     * Used to login user by FP using user name and user password
//     * @param email the user name
//     * @param password the user password
//     * @return user unique identifier
//     * @throws InvalidParameterException if if incoming parameters are not valid
//     * @throws AccountNotFoundException if user doesn't exist in DB
//     * @throws IncorrectPasswordException if password for such user is incorrect
//     */
//    @Deprecated
//    public Long loginByFP(final String email, final String password) throws InvalidParameterException,
//                                                                AccountNotFoundException, IncorrectPasswordException {
//        check(email != null, "The email should not be null");
//        check(password != null, "The password should not be null");
//
//        Account account = ofNullable(this.accountDAO.getByEmail(email)).orElseThrow(() -> new AccountNotFoundException(email));
//
//        if(!this.passwordEncoder.matches(password, account.getPassword())) {
//            throw new IncorrectPasswordException();
//        }
//        return account.getId();
//    }

    /**
     * Returns an account or creates new account if such an account not found
     *
     * @param authRequest the request for authentication via facebook
     * @return an account
     * @throws InvalidParameterException if the request contains incorrect data
     */
    public Account getOrCreateFbAccount(final AuthenticationSocialRequest authRequest) throws InvalidParameterException {
        check(authRequest != null, "The fb auth request should not be null");
        check(isNotEmpty(authRequest.getSocialId()), "The social ID should not be null/empty");
//        fbValidation(authRequest); // turn on when will be set the correct settings facebook app
        Account account = this.accountDAO.getByFbId(authRequest.getSocialId());
        if(account == null) {
            if(authRequest.getEmail() != null) {
                account = this.accountDAO.getByEmail(authRequest.getEmail());
            }
            if(account != null) {
                // check out that the required fields are filled or fill them if required
                boolean modified = false;
                if(account.getFbId() == null) {
                    account.setFbId(authRequest.getSocialId());
                    modified = true;
                }
                if(account.getUserName() == null) {
                    String username = ofNullable(authRequest.getUsername())
                            .orElse(generateUsername(authRequest.getFirstName(), authRequest.getLastName()));
                    account.setUserName(username);
                    modified = true;
                }
                if(account.getProfileImageUrl() == null && authRequest.getImage() != null) {
                    account.setProfileImageUrl(authRequest.getImage());
                    modified = true;
                }
//                if(authRequest.getToken() != null) {              // the token has expiration
//                    account.setFbToken(authRequest.getToken());
//                    modified = true;
//                }
                if (modified) {
                    account = this.accountDAO.update(account);
                }
            } else {
                // there is no account with such an email and with such a social ID
                checkEmail(authRequest.getEmail());
                account = new Account();
                account.setFbId(authRequest.getSocialId());
                account.setEmail(authRequest.getEmail());
                String username = ofNullable(authRequest.getUsername())
                        .orElse(generateUsername(authRequest.getFirstName(), authRequest.getLastName()));
                account.setUserName(username);
                account.setProfileImageUrl(authRequest.getImage());
//                account.setFbToken(authRequest.getToken());   // the token has expiration
                account = this.accountDAO.create(account);
            }
        }
        return account;
    }



//    public Account getOrCreateAccountForVkId(final String vkId) {
//        check(vkId != null, "The vk id should not be null");
//        Account account = this.accountDAO.getByVkId(vkId);
//        if(account == null) {
//            // no account at all
//            account = new Account();
//            account.setVkId(vkId);
//            account.setVkProfileUrl(VK_BASE_URL + "id" + vkId);
//            accountDAO.create(account);
//        }
//        return account;
//    }



    /**
     * Returns an account by the specified email
     *
     * @param email the email
     * @return the account and null otherwise
     * @throws InvalidParameterException if the email is invalid
     */
    public Account getByEmail(final String email) throws InvalidParameterException {
        checkNotNull(email, "email");
        return this.accountDAO.getByEmail(email);
    }

    /**
     * Returns an account by account ID or throw exception
     *
     * @param accountId the account ID
     * @return the account
     * @throws InvalidParameterException if the account ID is invalid
     * @throws AccountNotFoundException if the account doesn't exist
     */
    public Account getOrThrow(final Long accountId) throws InvalidParameterException, AccountNotFoundException {
        return ofNullable(this.get(accountId)).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    /**
     * Returns an account with its events by the account ID.
     *
     * @param accountId the account ID
     * @return an account with its events and null otherwise
     * @throws InvalidParameterException if the account ID is invalid
     * todo: delete it
     */
    @Deprecated
    public Account getWithEvents(final Long accountId) throws InvalidParameterException {
        checkAccount(accountId);
        return this.accountDAO.getWithEvents(accountId);
    }

    /**
     * Checks an email
     *
     * @param email the email
     * @throws InvalidParameterException if the email is empty
     */
    private void checkEmail(final String email) throws InvalidParameterException {
        check(isNotEmpty(email), "The email should not be null/empty");
    }

    /**
     * Checks a password
     *
     * @param password the password
     * @throws IncorrectPasswordException if the password is empty
     */
    private void checkPassword(final String password) throws IncorrectPasswordException {
        if (StringUtils.isEmpty(password)) {
            throw new IncorrectPasswordException("Password is empty");
        }
    }

    /**
     * Validates a request for authentication via facebook
     *
     * @param authRequest the request for authentication via facebook
     * @throws InvalidParameterException if the request contains incorrect data
     */
    private void fbValidation(final AuthenticationSocialRequest authRequest) throws InvalidParameterException {
        FBUserInfoResponse user = getFbUser(authRequest.getToken());
        check(user.getId().equals(authRequest.getSocialId()),
                "does not match the facebook ID in the transferred token and the transferred parameters");
        check(user.getEmail().equals(authRequest.getEmail()),
                "does not match the email in the transferred token and the transferred parameters");
    }

    /**
     * Returns a facebook info of the user by facebook token
     *
     * @param token the facebook token
     * @return a facebook info of the user
     * @throws InvalidParameterException if the facebook token is incorrect
     */
    private FBUserInfoResponse getFbUser(final String token) throws InvalidParameterException {
        String fbId = this.getFbId(token);

        FBUserInfoResponse userInfo = null;
        try {
            userInfo = restTemplate.getForObject(String.format(REQUEST_USER_INFO_URL, fbId, token), FBUserInfoResponse.class);
//            userInfo = restTemplate.getForObject(String.format(REQUEST_USER_INFO_URL_ME, token), FBUserInfoResponse.class);
        } finally {
            check(userInfo != null, "Unable to get user info from facebook");
            check(userInfo.hasError(), userInfo.getError().getMessage());
        }
        return userInfo;
    }

    /**
     * Returns the facebook ID of the user by facebook token
     *
     * @param token the facebook token
     * @return the facebook ID of the user
     * @throws InvalidParameterException if the facebook token is incorrect
     */
    private String getFbId(final String token) throws InvalidParameterException {
        FBUserTokenInfo tokenInfo = null;
        try {
            tokenInfo = this.restTemplate.getForObject(String.format(DEBUG_TOKEN_URL, token, appKey, appSecret), FBUserTokenInfo.class);
        } finally {
            check(tokenInfo != null, "Unable to verify token");
            check(tokenInfo.hasError(), tokenInfo.getData().getError().getMessage());
        }
        return tokenInfo.getData().getUserId();
    }

    /**
     * Generates an username
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return an username
     */
    private String generateUsername(final String firstName, final String lastName) {
        if(firstName == null && lastName == null) {
            return "Anonymous";
        }
        return firstName != null ? firstName : lastName;
    }

}
