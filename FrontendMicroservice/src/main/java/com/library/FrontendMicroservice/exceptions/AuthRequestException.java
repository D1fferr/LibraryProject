package com.library.FrontendMicroservice.exceptions;

public class AuthRequestException extends RuntimeException {
    public AuthRequestException(String message) {
        super(message);
    }
}
