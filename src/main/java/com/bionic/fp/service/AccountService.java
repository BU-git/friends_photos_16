package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.auth.impl.UserNameAlreadyExistException;
import com.bionic.fp.exception.logic.impl.AccountNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bionic.fp.util.Checks.check;
import static java.util.Optional.ofNullable;

/**
 * Created by boubdyk on 11.11.2015.
 */
@Named
@Transactional
public class AccountService {
    public static final String FACEBOOK_BASE_URL = "https://www.facebook.com/";
    private static final String VK_BASE_URL = "http://vk.com/";

    @Inject
    private AccountDAO accountDAO;

    public AccountService() {}

    public Long create(final Account account) throws InvalidParameterException {
        check(account != null, "The account should not be null");
        return this.accountDAO.create(account);
    }

    /**
     * Used to register user by FP. If user exist method return user id. If user doesn't exist method
     * create new user in DB and return his id
     * @param email the users email
     * @param password the user password
     * @return unique user identifier
     * @throws UserNameAlreadyExistException if user already exist in DB
     * @throws IncorrectPasswordException if user password is incorrect
     */
    public Long registerByFP(final String email, final String password) throws EmailAlreadyExistException, IncorrectPasswordException {
        checkEmail(email);
        checkPassword(password);
        if(this.accountDAO.getByEmail(email) != null) {
            throw new EmailAlreadyExistException(email);
        }

        Account account = new Account(email, password);
        return this.accountDAO.create(account);
    }

    /**
     * Used to login user by FP using user name and user password
     * @param email the user name
     * @param password the user password
     * @return user unique identifier
     * @throws InvalidParameterException if if incoming parameters are not valid
     * @throws AccountNotFoundException if user doesn't exist in DB
     * @throws IncorrectPasswordException if password for such user is incorrect
     */
    public Long loginByFP(final String email, final String password) throws InvalidParameterException,
                                                                AccountNotFoundException, IncorrectPasswordException {
        check(email != null, "The email should not be null");
        check(password != null, "The password should not be null");

        Account account = ofNullable(this.accountDAO.getByEmail(email)).orElseThrow(() -> new AccountNotFoundException(email));

        if(!Objects.equals(account.getPassword(), password)) {
            throw new IncorrectPasswordException();
        }

        account.setActive(true); // ?
        this.accountDAO.update(account);

        return account.getId();
    }

    /**
     * Get account with specified facebook id or email, or creates new account if such account not found
     * @param fbId facebook id
     * @param name user name
     * @param email user email
     * @return existing or new account
     */
    public Account getOrCreateAccountForFbId(final String fbId, final String name, final String email) {
        check(fbId != null, "The fb id should not be null");
        Account account = this.accountDAO.getByFbId(fbId);
        if(account == null) {
            if (email != null && !"".equals(email.trim())) {
                account = this.accountDAO.getByEmail(email);
                if(account != null) {
                    account.setFbId(fbId);
                    account.setFbProfileUrl(FACEBOOK_BASE_URL + fbId);
                    return account;
                }
            }

            // no account at all
            account = new Account();
            account.setFbId(fbId);
            account.setEmail(email);
            account.setUserName(name);
            account.setFbProfileUrl(FACEBOOK_BASE_URL + fbId);
            accountDAO.create(account);
        }
        return account;
    }

    public Account getOrCreateAccountForVkId(final String vkId) {
        check(vkId != null, "The vk id should not be null");
        Account account = this.accountDAO.getByVkId(vkId);
        if(account == null) {
            // no account at all
            account = new Account();
            account.setVkId(vkId);
            account.setVkProfileUrl(VK_BASE_URL + "id" + vkId);
            accountDAO.create(account);
        }
        return account;
    }

    /**
     * Returns an account by the account ID
     *
     * @param accountId the account ID
     * @return the event and null otherwise
     * @throws InvalidParameterException if the account ID is invalid
     */
    public Account get(final Long accountId) throws InvalidParameterException {
        this.validation(accountId);
        return this.accountDAO.read(accountId);
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
     */
    public Account getWithEvents(final Long accountId) throws InvalidParameterException {
        this.validation(accountId);
        return this.accountDAO.getWithEvents(accountId);
    }

    /**
     * Returns a list of the events by the account ID
     *
     * @param accountId the account ID
     * @return a list of the events of the account
     * @throws AccountNotFoundException if the account ID is invalid
     */
    public List<Event> getEvents(final Long accountId) throws InvalidParameterException, AccountNotFoundException {
        this.validation(accountId);
        return this.accountDAO.getEvents(accountId);
    }

    public List<Long> getEventIds(final Long accountId) {
        List<Event> events = this.getEvents(accountId);
        return events.stream().parallel().map(Event::getId).collect(Collectors.toList());
    }

    /**
     * Checks an account ID
     *
     * @param accountId the account ID
     * @throws InvalidParameterException if the account ID is invalid
     */
    private void validation(final Long accountId) throws InvalidParameterException {
        check(accountId != null, "The account ID should not be null");
    }

    /**
     * Checks an email
     * @param email the email
     * @throws InvalidParameterException if the email is empty
     */
    private void checkEmail(final String email) throws InvalidParameterException {
        check(StringUtils.isNotEmpty(email), "The email should not be null/empty");
    }

    /**
     * Checks a password
     * @param password the password
     * @throws IncorrectPasswordException if the password is empty
     */
    private void checkPassword(final String password) throws IncorrectPasswordException {
        if (StringUtils.isEmpty(password)) {
            throw new IncorrectPasswordException("Password is empty");
        }
    }

    /**
     * Method is used to check if user exist in DB.
     * @param email the users email
     * @param fbId the users fb id
     * @param vkId the users vk id
     * @return true if user exist and false otherwise
     */
    private boolean isExist(final String email, final String fbId, final String vkId) {
        if (email != null) {
            return ofNullable(this.accountDAO.getByEmail(email)).isPresent();
        }
        if (fbId != null) {
            return ofNullable(this.accountDAO.getByFbId(fbId)).isPresent();
        }
        if (vkId != null) {
            return ofNullable(this.accountDAO.getByVkId(vkId)).isPresent();
        }
        return false;
    }
}
