package ua.zakharchuk.ExpectedBooksService.exceptions;

public class ExpectedBooksNotFoundException extends RuntimeException {
    public ExpectedBooksNotFoundException(String message) {
        super(message);
    }
}
