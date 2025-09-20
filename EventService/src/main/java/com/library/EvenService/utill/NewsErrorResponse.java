package com.library.EvenService.utill;

import org.springframework.stereotype.Component;

@Component
public class NewsErrorResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
