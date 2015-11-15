package com.bionic.fp.service;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.entity.Account;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Named
@Transactional
public class AccountService {

    @Inject
    private AccountDAO accountsDAO;

    public AccountService() {}

    public Long addAccount(Account account) {
        if (account == null) return null;
        return accountsDAO.create(account);
    }

}
