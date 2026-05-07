package com.Library.AuthService.exceptions;

public class FailedToConnectWithRedisException extends RuntimeException {
    public FailedToConnectWithRedisException(String message) {
        super(message);
    }
}
