package com.library.ServiceCatalog.util;

public class BookAlreadyExitException extends RuntimeException {
    public BookAlreadyExitException(String message) {
        super(message);
    }
}
