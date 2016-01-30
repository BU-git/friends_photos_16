package com.bionic.fp.dao.impl;

import com.bionic.fp.dao.AccountDAO;
import com.bionic.fp.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an implementation of {@link AccountDaoImpl}
 *
 * @author Sergiy Gabriel
 */
@Repository
public class AccountDaoImpl extends GenericDaoJpaImpl<Account, Long> implements AccountDAO {

    private static final String EMAIL = "email";
    private static final String FB_ID = "fbId";
    private static final String VK_ID = "vkId";

    public AccountDaoImpl() {}

    @Override
    @Deprecated
    public Account getWithEvents(final Long accountId) {
        EntityGraph graph = this.em.getEntityGraph("Account.events");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.em.find(Account.class, accountId, hints);
    }

    @Override
    public Account getByEmail(final String email) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(EMAIL, email)));
    }

    @Override
    public Account getByFbId(final String fbId) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(FB_ID, fbId)));
    }

    @Override
    public Account getByVkId(final String vkId) {
        return this.getSingleResult(this.em.createQuery(this.getQuery(VK_ID, vkId)));
    }
}
