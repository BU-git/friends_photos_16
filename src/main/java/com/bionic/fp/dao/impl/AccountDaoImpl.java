package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is an implementation of {@link AccountDaoImpl}
 *
 * @author Sergiy Gabriel
 */
@Repository
@Transactional
public class AccountDaoImpl extends GenericDaoJpaImpl<Account, Long> implements AccountDAO {

    private static final String EMAIL = "email";
    private static final String FB_ID = "fbId";
    private static final String VK_ID = "vkId";

    public AccountDaoImpl() {}

    @Override
    @Transactional(readOnly = true)
    public Account getByEmail(final String email) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(EMAIL, email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByFbId(final String fbId) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(FB_ID, fbId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByVkId(final String vkId) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(VK_ID, vkId)));
    }
}
