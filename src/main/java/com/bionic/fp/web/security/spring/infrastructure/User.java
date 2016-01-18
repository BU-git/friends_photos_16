package com.bionic.fp.web.security.spring.infrastructure;

import com.bionic.fp.domain.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides core user information
 *
 * @author Sergiy Gabriel
 */
public class User implements UserDetails {

    private static final long serialVersionUID = -5740036072965509658L;

    private Long id;
    private String email;
    private String password;

    public User() {
    }

    public User(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }

    public User(final Long id, final String email, final String password) {
        this(id, email);
        this.password = password;
    }

    public User(final Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validate() {
        return this.id != null && this.email != null;
    }
}
