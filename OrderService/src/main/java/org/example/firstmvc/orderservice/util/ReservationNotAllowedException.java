package org.example.firstmvc.orderservice.util;

public class ReservationNotAllowedException extends RuntimeException {
    public ReservationNotAllowedException(String message) {
        super(message);
    }
}
