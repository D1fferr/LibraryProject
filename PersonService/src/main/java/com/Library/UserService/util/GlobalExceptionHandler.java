package com.Library.UserService.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final UserErrorResponse userErrorResponse;

    public GlobalExceptionHandler(UserErrorResponse userErrorResponse) {
        this.userErrorResponse = userErrorResponse;
    }
    @ExceptionHandler(UsersNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUsersNotFoundException(UsersNotFoundException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotCreatedException(UserNotCreatedException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<UserErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<UserErrorResponse> handleBadCredentialsException(BadCredentialsException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotChangeException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotChangeException(UserNotChangeException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
