package com.bionic.fp.service.impl;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Role;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.logic.impl.AccountEventNotFoundException;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.service.RoleService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public abstract class AbstractMethodSecurityService implements MethodSecurityService {

    protected final RoleService roleService;
    protected final AccountService accountService;

    protected AbstractMethodSecurityService(final AccountService accountService, final RoleService roleService) throws RuntimeException {
        this.accountService = ofNullable(accountService)
                .orElseThrow(() -> new RuntimeException("The injection of the account service did not happen"));
        this.roleService = ofNullable(roleService)
                .orElseThrow(() -> new RuntimeException("The injection of the role service did not happen"));
    }

    @Override
    @SafeVarargs
    public final void checkPermission(final Long eventId, final Predicate<Role>... predicates) throws InvalidParameterException, AccessDeniedException {
        if(isNotEmpty(predicates)) {
            try {
                Role role = this.roleService.getRole(this.getUserId(), eventId);
                if(Stream.of(predicates).anyMatch(p -> p.negate().test(role))) {
                    throw new AccessDeniedException("You are not allowed to perform this operation");
                }
            } catch (AccountEventNotFoundException e) {
                throw new AccessDeniedException("You are not allowed to perform this operation");
            }
        }
    }

    @Override
    public Account getUser() throws DisabledException {
        return ofNullable(this.accountService.get(this.getUserId())).orElseThrow(() ->
                new DisabledException("User is disabled"));
    }
}
