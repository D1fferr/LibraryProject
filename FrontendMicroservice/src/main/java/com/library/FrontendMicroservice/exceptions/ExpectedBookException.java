package com.library.FrontendMicroservice.exceptions;

public class ExpectedBookException extends RuntimeException {
    public ExpectedBookException(String message) {
        super(message);
    }
}
