package com.library.ServiceCatalog.exceptions;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final BookErrorResponse bookErrorResponse;

    public GlobalExceptionHandler(BookErrorResponse bookErrorResponse) {
        this.bookErrorResponse = bookErrorResponse;
    }

    @ExceptionHandler(BookNotCreatedException.class)
    public ResponseEntity<BookErrorResponse> handleBookNotCreatedException(BookNotCreatedException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookAlreadyExistException.class)
    public ResponseEntity<BookErrorResponse> handleBookAlreadyExitException(BookAlreadyExistException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookErrorResponse> handleBookNotFoundException(BookNotFoundException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BooksNotFoundException.class)
    public ResponseEntity<BookErrorResponse> handleBooksNotFoundException(BooksNotFoundException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FailedSaveImageException.class)
    public ResponseEntity<BookErrorResponse> handleFailedSaveImageException(FailedSaveImageException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BookErrorResponse> handleIllegalArgumentException(IllegalArgumentException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<BookErrorResponse> handleRedisConnectionFailureException(RedisConnectionFailureException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CategoriesNotFoundException.class)
    public ResponseEntity<BookErrorResponse> handleCategoriesNotFoundException(CategoriesNotFoundException e){
        bookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
