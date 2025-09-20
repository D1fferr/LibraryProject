package com.library.ServiceCatalog.util;

public class BookNotCreatedException extends RuntimeException {
    public BookNotCreatedException(String message) {
        super(message);
    }
}
