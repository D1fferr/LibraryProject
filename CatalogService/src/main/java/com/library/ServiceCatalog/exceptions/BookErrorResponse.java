package com.library.ServiceCatalog.exceptions;

import org.springframework.stereotype.Component;

@Component
public class BookErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
