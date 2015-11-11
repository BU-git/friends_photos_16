package com.bionic.fp.service;

import com.bionic.fp.dao.AccountsDAO;
import com.bionic.fp.entity.Accounts;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Named
@Transactional
public class AccountsService {

    @Inject
    private AccountsDAO accountsDAO;

    public AccountsService() {}

    public Long addAccount(Accounts account) {
        if (account == null) return null;
        return accountsDAO.create(account);
    }
}
