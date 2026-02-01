package com.library.orderservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ReservationErrorResponse response;

    public GlobalExceptionHandler(ReservationErrorResponse response) {
        this.response = response;
    }

    @ExceptionHandler(ReservationNotCreatedException.class)
    public ResponseEntity<ReservationErrorResponse> handleReservationNotCreatedException(ReservationNotCreatedException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationNotAllowedException.class)
    public ResponseEntity<ReservationErrorResponse> handleReservationNotAllowedException(ReservationNotAllowedException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ReservationErrorResponse> handleBookNotFoundException(BookNotFoundException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ReservationErrorResponse> handleReservationNotFoundException(ReservationNotFoundException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ReservationErrorResponse> handleEmailSendingException(EmailSendingException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(ReservationsNotFoundException.class)
    public ResponseEntity<ReservationErrorResponse> handleEmailSendingException(ReservationsNotFoundException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<ReservationErrorResponse> handleKafkaException(KafkaException e){
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
