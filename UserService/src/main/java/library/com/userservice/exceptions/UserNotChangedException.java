package library.com.userservice.exceptions;

public class UserNotChangedException extends RuntimeException {
    public UserNotChangedException(String message) {
        super(message);
    }
}