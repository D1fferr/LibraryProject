package com.Library.UserService.exceptions;

public class FailedToConnectWithRedisException extends RuntimeException {
    public FailedToConnectWithRedisException(String message) {
        super(message);
    }
}
