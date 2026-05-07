package com.Library.AuthService.exceptions;

public class FailedToConnectWithUserServiceException extends RuntimeException {
    public FailedToConnectWithUserServiceException(String message) {
        super(message);
    }
}
