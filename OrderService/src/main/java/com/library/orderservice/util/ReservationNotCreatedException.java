package com.library.orderservice.util;

public class ReservationNotCreatedException extends RuntimeException {
    public ReservationNotCreatedException(String message) {
        super(message);
    }
}
