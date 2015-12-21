package com.bionic.fp.web.rest;

import com.bionic.fp.exception.AppException;
import com.bionic.fp.exception.logic.critical.NonUniqueResultException;
import com.bionic.fp.exception.permission.PermissionsDeniedException;
import com.bionic.fp.exception.permission.UserDoesNotExistException;
import com.bionic.fp.exception.auth.impl.InvalidSessionException;
import com.bionic.fp.exception.logic.EntityNotFoundException;
import com.bionic.fp.exception.logic.InvalidParameterException;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.web.rest.dto.ErrorInfoDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Global exception handler of the application
 *
 * @author Sergiy Gabriel
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({InvalidParameterException.class, EntityNotFoundException.class})
    @ResponseStatus(BAD_REQUEST)
    public @ResponseBody ErrorInfoDTO badRequestExceptionHandler(AppException e) {
        return new ErrorInfoDTO(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public void notFoundExceptionHandler(NotFoundException e) {
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseStatus(BAD_REQUEST)
    public @ResponseBody ErrorInfoDTO userNotFoundInEvent(UserDoesNotExistException e) {
        return new ErrorInfoDTO(e.getMessage());
    }

    @ExceptionHandler(PermissionsDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public @ResponseBody ErrorInfoDTO permissionsDenied(PermissionsDeniedException e){
        return new ErrorInfoDTO(e.getMessage());
    }

    @ExceptionHandler(InvalidSessionException.class)
    @ResponseStatus(UNAUTHORIZED)
    public @ResponseBody ErrorInfoDTO unauthorizedExceptionHandler(InvalidSessionException e){
        return new ErrorInfoDTO(e.getMessage());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    @ResponseStatus(CONFLICT)
    public @ResponseBody ErrorInfoDTO nonUniqueResultExceptionHandler(NonUniqueResultException e){
        return new ErrorInfoDTO(e.getMessage());
    }
}
