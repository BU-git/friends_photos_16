package com.bionic.fp.rest;

import com.bionic.fp.exception.PermissionsDeniedException;
import com.bionic.fp.exception.UserDoesNotExistException;
import com.bionic.fp.exception.app.logic.EntityNotFoundException;
import com.bionic.fp.exception.app.logic.InvalidParameterException;
import com.bionic.fp.exception.app.rest.NotFoundException;
import com.bionic.fp.rest.dto.ErrorInfoDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public @ResponseBody ErrorInfoDTO entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return new ErrorInfoDTO(e.getMessage());
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public @ResponseBody ErrorInfoDTO badRequestExceptionHandler(InvalidParameterException e) {
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
}
