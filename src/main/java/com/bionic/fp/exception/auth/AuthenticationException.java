package com.bionic.fp.exception.auth;

import com.bionic.fp.exception.AppException;

/**
 * Signals the error during authorization/registration
 *
 * @author Sergiy Gabriel
 */
public class AuthenticationException extends AppException {

    public AuthenticationException(final String message) {
        super(message);
    }

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}
}