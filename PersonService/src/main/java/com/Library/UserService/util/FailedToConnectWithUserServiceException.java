package com.Library.UserService.util;

public class FailedToConnectWithUserServiceException extends RuntimeException {
    public FailedToConnectWithUserServiceException(String message) {
        super(message);
    }
}
