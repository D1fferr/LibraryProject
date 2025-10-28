package library.com.userservice1.exceptions;

public class UserNotChangedException extends RuntimeException {
    public UserNotChangedException(String message) {
        super(message);
    }
}
