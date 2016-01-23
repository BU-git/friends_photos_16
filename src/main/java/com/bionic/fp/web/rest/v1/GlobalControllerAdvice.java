package com.bionic.fp.web.rest.v1;

import com.bionic.fp.exception.AppException;
import com.bionic.fp.exception.auth.AuthenticationException;
import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.IncorrectPasswordException;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.permission.PermissionsDeniedException;
import com.bionic.fp.exception.permission.UserDoesNotExistInEventException;
import com.bionic.fp.exception.auth.impl.InvalidSessionException;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.web.rest.dto.ErrorInfo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Global exception handler of the application
 *
 * @author Sergiy Gabriel
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({
            InvalidParameterException.class,
            EntityNotFoundException.class,
            EmailAlreadyExistException.class,
            IncorrectPasswordException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorInfo badRequestExceptionHandler(AppException e) {
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public void notFoundExceptionHandler(NotFoundException e) {
    }

    @ExceptionHandler(UserDoesNotExistInEventException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorInfo userNotFoundInEvent(UserDoesNotExistInEventException e) {
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler(PermissionsDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    public ErrorInfo permissionsDenied(PermissionsDeniedException e){
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    public ErrorInfo accessDenied(AccessDeniedException e){
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler({InvalidSessionException.class, AuthenticationException.class})
    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    public ErrorInfo unauthorizedExceptionHandler(InvalidSessionException e){
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class, org.springframework.security.core.AuthenticationException.class})
    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    public ErrorInfo unauthorizedExceptionHandler(org.springframework.security.core.AuthenticationException e){
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    @ResponseStatus(CONFLICT)
    @ResponseBody
    public ErrorInfo nonUniqueResultExceptionHandler(NonUniqueResultException e){
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo IOExceptionHandler(IOException e){
        return new ErrorInfo(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorInfo authenticationExceptionHandler(BadCredentialsException e){
        return new ErrorInfo(e.getMessage());
    }
}
