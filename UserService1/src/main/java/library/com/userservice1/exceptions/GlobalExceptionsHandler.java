package library.com.userservice1.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionsHandler {
    private final UserErrorResponse userErrorResponse;

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotCreatedException(UserNotCreatedException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotChangedException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotChangedException(UserNotChangedException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        userErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
