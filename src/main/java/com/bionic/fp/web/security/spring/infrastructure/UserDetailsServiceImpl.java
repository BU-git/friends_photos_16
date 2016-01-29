package com.bionic.fp.web.security.spring.infrastructure;

import com.bionic.fp.domain.Account;
import com.bionic.fp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

/**
 * This is implementation of {@link UserDetailsService} which loads user details by email
 *
 * @author Sergiy Gabriel
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Autowired
    public UserDetailsServiceImpl(final AccountService accountService) throws RuntimeException {
        this.accountService = ofNullable(accountService)
                .orElseThrow(() -> new RuntimeException("The injection of the account service did not happen"));
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return this.loadUserByEmail(email);
    }

    /**
     * Returns an user by email
     *
     * @param email the user email
     * @return an user
     * @throws UsernameNotFoundException if the user doesn't exists
     */
    public User loadUserByEmail(final String email) throws UsernameNotFoundException {
        Account account = ofNullable(this.accountService.getByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(account.getId(), account.getEmail(), account.getPassword());
    }

}
