package com.library.orderservice.util;

public class ReservationsNotFoundException extends RuntimeException {
    public ReservationsNotFoundException(String message) {
        super(message);
    }
}
