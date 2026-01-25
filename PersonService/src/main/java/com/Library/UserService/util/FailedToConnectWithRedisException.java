package com.Library.UserService.util;

public class FailedToConnectWithRedisException extends RuntimeException {
    public FailedToConnectWithRedisException(String message) {
        super(message);
    }
}
