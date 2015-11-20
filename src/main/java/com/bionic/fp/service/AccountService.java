package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

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

    public Long addAccount(Account account) {
        if (account == null) return null;
        return accountDAO.create(account);
    }

    /**
     * Method is used to chek if user exist in DB.
     * @param email users email.
     * @param fbID users fb id.
     * @param vkID users vk id.
     * @return true if user exist else return false.
     */
    private boolean isExist(String email, String fbID, String vkID) {
        Account account;
        if (email != null) {
            try {
                account = accountDAO.getByEmail(email);
                return true;
            } catch (NoResultException nre) {
                return false;
            }
//            return accountDAO.getByEmail(email) != null ? true : false;
        }
        if (fbID != null) {
            try {
                account = accountDAO.getByFBId(fbID);
                return true;
            } catch (NoResultException nre) {
                return false;
            }
//            return accountDAO.getByFBId(fbID) != null ? true : false;
        }
        if (vkID != null) {
            try {
                account = accountDAO.getByVKId(vkID);
                return true;
            } catch (NoResultException nre) {
                return false;
            }
//            return accountDAO.getByVKId(vkID) != null ? true : false;
        }
        return false;
    }

    /**
     * Used to register user by FP. If user exist method return user id. If user doesn't exist method
     * create new user in DB and return his id.
     * @param email users email.
     * @param userName user name.
     * @param password user password.
     * @return unique user identifier.
     * @throws UserNameAlreadyExistException if user already exist in DB.
     * @throws EmptyPasswordException if user password is empty.
     */
    public Long registerByFP(String email, String userName, String password) throws UserNameAlreadyExistException,
            EmptyPasswordException, EmailAlreadyExistException {
        if (isExist(email, null, null)) {
            throw new EmailAlreadyExistException();
        }
        Account account;
        try {
            account = accountDAO.getByUserName(userName);
        } catch (NoResultException nre) {
            account = null;
        }
        if (account != null) {
            throw new UserNameAlreadyExistException();
        }
        if (StringUtils.isEmpty(password)) {
            throw new EmptyPasswordException();
        }
        account = new Account(email, userName, password);
        return accountDAO.create(account);

    }

    /**
     * Used to login user by FP using user name and user password.
     * @param userName user name.
     * @param password user password in FP.
     * @return user unique identifier.
     * @throws EmptyPasswordException if password is empty.
     * @throws UserDoesntExistException if user doesn't exist in DB.
     * @throws IncorrectPasswordException if password for such user is incorrect.
     */
    public Long loginByFP(String userName, String password) throws UserDoesntExistException,
            IncorrectPasswordException, EmptyPasswordException {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new EmptyPasswordException();
        }
        Account account;
        try {
            account  = accountDAO.getByUserName(userName);
        } catch (NoResultException nre) {
            account = null;
        }
        if (account == null) {
            throw new UserDoesntExistException();
        }
        if (!(account.getPassword().intern() == password.intern())) {
            throw new IncorrectPasswordException();
        }
        account.setActive(true);
        accountDAO.update(account);
        return account.getId();
    }

    /**
     * Get account with specified facebook id or email, or creates new account if such account not found
     * @param facebookId facebook id
     * @param name user name
     * @param email user email
     * @return existing or new account
     */
    public Account getOrCreateAccountForFBId(String facebookId, String name, String email) {
        Account account;
        try {
            account = accountDAO.getByFBId(facebookId);
            return account;
        } catch (NoResultException ignored) {}

        if (email != null && !"".equals(email.trim())) {
            try {
                account = accountDAO.getByEmail(email);
                account.setFacebookId(facebookId);
                account.setFacebookProfileUrl(FACEBOOK_BASE_URL + facebookId);
                return account;
            } catch (NoResultException ignored) {}
        }

        // no account at all
        account = new Account();
        account.setUserName(name);
        account.setEmail(email);
        account.setFacebookId(facebookId);
        account.setFacebookProfileUrl(FACEBOOK_BASE_URL + facebookId);
        accountDAO.create(account);
        return account;
    }

    public Account getOrCreateAccountForVKId(String vkId) {
        Account account;
        try {
            account = accountDAO.getByVKId(vkId);
            return account;
        } catch (NoResultException ignored) {}

        // no account at all
        account = new Account();
        account.setVkId(vkId);
        account.setVkProfileUrl(VK_BASE_URL + "id" + vkId);
        accountDAO.create(account);
        return account;
    }

    public Account getById(final Long id) {
        return id == null ? null : this.accountDAO.read(id);
    }

    public Account getByIdWithGroups(final Long id) {
        return id == null ? null : this.accountDAO.readWithGroups(id);
    }
}
