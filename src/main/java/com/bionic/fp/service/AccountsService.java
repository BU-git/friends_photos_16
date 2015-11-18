package com.bionic.fp.service;

import com.bionic.fp.dao.AccountsDAO;
import com.bionic.fp.domain.Account;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Named
@Transactional
public class AccountsService {
    public static final String FACEBOOK_BASE_URL = "https://www.facebook.com/";

    @Inject
    private AccountsDAO accountsDAO;

    public AccountsService() {}

    public Long addAccount(Account account) {
        if (account == null) return null;
        return accountsDAO.create(account);
    }

    /**
     * Get account with specified facebook id or email, or creates new account if such account not found
     * @param facebookId facebook id
     * @param name user name
     * @param email user email
     * @return existing or new account
     */
    public Account getOrCreateAccountForFBId(String facebookId, String name, String email) {
        Account account = accountsDAO.getAccountByFBId(facebookId);
        if (account != null) {
            return account;
        }

        account = accountsDAO.getAccountByEmail(email);
        if (account != null) { // there is with such email but without facebook id
            account.setFacebookId(facebookId);
            account.setFacebookProfileUrl(FACEBOOK_BASE_URL + facebookId);
            return account;
        }

        // no account at all
        account = new Account();
        account.setUserName(name);
        account.setEmail(email);
        account.setFacebookId(facebookId);
        account.setFacebookProfileUrl(FACEBOOK_BASE_URL + facebookId);
        account.setActive(true);
        accountsDAO.create(account);
        return account;
    }
}
