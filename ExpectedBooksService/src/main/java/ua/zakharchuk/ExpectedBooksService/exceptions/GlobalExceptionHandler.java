package ua.zakharchuk.ExpectedBooksService.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final ExpectedBookErrorResponse expectedBookErrorResponse;

    @ExceptionHandler(ExpectedBookNotFoundException.class)
    public ResponseEntity<ExpectedBookErrorResponse> handleExpectedBookNotFoundException(ExpectedBookNotFoundException e){
        expectedBookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(expectedBookErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ExpectedBooksNotFoundException.class)
    public ResponseEntity<ExpectedBookErrorResponse> handleExpectedBooksNotFoundException(ExpectedBooksNotFoundException e){
        expectedBookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(expectedBookErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ExpectedBookNotCreatedException.class)
    public ResponseEntity<ExpectedBookErrorResponse> handleExpectedBookNotCreatedException(ExpectedBookNotCreatedException e){
        expectedBookErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(expectedBookErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
