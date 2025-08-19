package com.library.ServiceCatalog.util;

import jakarta.persistence.Column;
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
