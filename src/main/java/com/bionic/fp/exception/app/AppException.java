package com.bionic.fp.exception.app;

/**
 * Base class for all application-specific exceptions.
 * Signals about exceptional cases in the application logic
 *
 * @author Sergiy Gabriel
 */
public class AppException extends RuntimeException {

    public AppException(final String message) {
        super(message);
    }

    public AppException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AppException(final Throwable cause) {
        super(cause);
    }
}
