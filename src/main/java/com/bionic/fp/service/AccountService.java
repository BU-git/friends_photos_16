package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.entity.Account;
import com.bionic.fp.exception.*;
import org.apache.commons.lang.StringUtils;
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
                account = accountDAO.getByFB(fbID);
                return true;
            } catch (NoResultException nre) {
                return false;
            }
//            return accountDAO.getByFB(fbID) != null ? true : false;
        }
        if (vkID != null) {
            try {
                account = accountDAO.getByVK(vkID);
                return true;
            } catch (NoResultException nre) {
                return false;
            }
//            return accountDAO.getByVK(vkID) != null ? true : false;
        }
        return false;
    }

    /**
     * Used to register user by FB. If user exist method return user id. If user doesn't exist method
     * create new user in DB and return his id.
     * @param fbID users FB unique identifier.
     * @param fbProfile users FB url.
     * @param fbToken users FB token.
     * @param userName users name from FB.
     * @return user unique identifier.
     */
    public Long loginByFB(String fbID, String fbProfile, String fbToken, String userName) {
        if (isExist(null, fbID, null)) {
            Account account = accountDAO.getByFB(fbID);
            account.setActive(true);
            accountDAO.update(account);
            return account.getId();
        } else {
            Account account = new Account(true, fbID, fbProfile, fbToken, userName);

            //TODO Set nullable true for email!!!
            account.setEmail("mylo!!!");

            return accountDAO.create(account);
        }
    }

    /**
     * Used to register user by VK. If user exist method return user id. If user doesn't exist method
     * create new user in DB and return his id.
     * @param vkID users VK unique identifier.
     * @param vkProfile users VK url.
     * @param vkToken users VK token.
     * @param userName users name from VK.
     * @return user unique identifier.
     */
    public Long loginByVK(String vkID, String vkProfile, String vkToken, String userName) {
        if (isExist(null, null, vkID)) {
            Account account = accountDAO.getByVK(vkID);
            account.setActive(true);
            accountDAO.update(account);
            return account.getId();
        } else {
            Account account = new Account(vkID, vkProfile, vkToken, userName);
            account.setActive(true);

            //TODO Set nullable true for email!!!
            account.setEmail("mylo");

            return accountDAO.create(account);
        }
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
        account = new Account(true, email, userName, password);
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
}
