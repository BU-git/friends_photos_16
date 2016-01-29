package com.bionic.fp.service.impl;

import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.RoleService;
import com.bionic.fp.web.security.spring.infrastructure.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@Service
public class SpringMethodSecurityService extends AbstractMethodSecurityService {

    @Autowired
    public SpringMethodSecurityService(final AccountService accountService, final RoleService roleService) throws RuntimeException {
        super(accountService, roleService);
    }

    @Override
    public Long getUserId() {
        return this.getUserDetails().getId();
    }

    @Override
    public String getUserEmail() {
        return this.getUserDetails().getEmail();
    }

    /**
     * Returns an user details
     *
     * @return an user details
     */
    private User getUserDetails() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
