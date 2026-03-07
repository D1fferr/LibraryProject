package com.library.ServiceCatalog.exceptions;

public class CategoriesNotFoundException extends RuntimeException {
    public CategoriesNotFoundException(String message) {
        super(message);
    }
}
