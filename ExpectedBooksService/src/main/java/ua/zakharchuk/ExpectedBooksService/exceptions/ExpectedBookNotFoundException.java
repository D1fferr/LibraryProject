package ua.zakharchuk.ExpectedBooksService.exceptions;

public class ExpectedBookNotFoundException extends RuntimeException {
    public ExpectedBookNotFoundException(String message) {
        super(message);
    }
}
