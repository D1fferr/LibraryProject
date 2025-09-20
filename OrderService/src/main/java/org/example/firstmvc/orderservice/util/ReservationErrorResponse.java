package org.example.firstmvc.orderservice.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ReservationErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
