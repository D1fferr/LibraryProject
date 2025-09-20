package com.library.ServiceCatalog.util;

import com.library.ServiceCatalog.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ExceptionHandler(BookAlreadyExitException.class)
    public ResponseEntity<BookErrorResponse> handleBookAlreadyExitException(BookAlreadyExitException e){
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


}
