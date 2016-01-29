package com.bionic.fp.web.security.spring.infrastructure.utils;

import com.bionic.fp.Constants.RestConstants.ACCOUNT;
import com.bionic.fp.web.security.spring.infrastructure.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Contains token utils for security
 *
 * @author Sergiy Gabriel
 */
@Component
public class TokenUtils {

    private final String secret;
    private final long expiration;

    @Autowired
    public TokenUtils(@Value("${token.secret}") final String secret,
                      @Value("${token.expiration}") final Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * Validates the token using the user details
     *
     * @param token the token
     * @param user the user details
     * @return true if the token is valid and false otherwise
     */
    public boolean validateToken(final String token, final User user) {
        if(!this.validateToken(token)) {
            return false;
        }
//        final String email = this.getUserEmail(token);
//        final Long userId = this.getUserId(token);
//        return user.getEmail().equals(email) && user.getId().equals(userId);
        return true;
    }

    /**
     * Validates the token
     *
     * @param token the token
     * @return true if the token is valid and false otherwise
     */
    public boolean validateToken(final String token) {
        if(token == null) {
            return false;
        }
        final Date expiration = this.getExpirationDate(token);
        return expiration != null && expiration.after(new Date(System.currentTimeMillis()));
    }

    /**
     * Returns a generated token using the user details
     *
     * @param user the user details
     * @return a generated token
     */
    public String generateToken(final User user) {
        return Jwts.builder()
                .claim(ACCOUNT.ID, user.getId())
                .claim(ACCOUNT.EMAIL, user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Returns an user id by the specified token
     *
     * @param token the token
     * @return an user id and null if the token is invalid
     */
    public Long getUserId(final String token) {
        Long userId = null;
        Claims body = this.getBody(token);
        if(body != null) {
            userId = Long.valueOf(body.get(ACCOUNT.ID).toString());
        }
        return userId;
    }

    /**
     * Returns an email of the user by the specified token
     *
     * @param token the token
     * @return an email and null if the token is invalid
     */
    public String getUserEmail(final String token) {
        String email = null;
        Claims body = this.getBody(token);
        if(body != null) {
            email = body.get(ACCOUNT.EMAIL).toString();
        }
        return email;
    }

    /**
     * Returns an user by the specified token
     *
     * @param token the token
     * @return an user and null if the token is invalid
     */
    public User extractUser(final String token) {
        User user = null;
        Claims body = this.getBody(token);
        if(body != null) {
            Long userId = Long.valueOf(body.get(ACCOUNT.ID).toString());
            String email = body.get(ACCOUNT.ID).toString();
            user =  new User(userId, email);
        }
        return user;
    }

    /**
     * Returns an expiration date by the specified token
     *
     * @param token the token
     * @return an expiration date and null if the token is invalid
     */
    private Date getExpirationDate(final String token) {
        Date expiration = null;
        Claims body = this.getBody(token);
        if(body != null) {
            expiration = body.getExpiration();
        }
        return expiration;
    }

    private Claims getBody(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception ignored) {
            return null;
        }
    }
}
