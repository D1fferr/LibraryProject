package com.Library.UserService.exceptions;

public class FailedToConnectWithUserServiceException extends RuntimeException {
    public FailedToConnectWithUserServiceException(String message) {
        super(message);
    }
}
