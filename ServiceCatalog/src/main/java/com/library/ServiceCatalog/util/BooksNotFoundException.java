package com.library.ServiceCatalog.util;

public class BooksNotFoundException extends RuntimeException {
    public BooksNotFoundException(String message) {
        super(message);
    }
}
