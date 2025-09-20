package ua.zakharchuk.ExpectedBooksService.exceptions;

public class ExpectedBookNotCreatedException extends RuntimeException {
    public ExpectedBookNotCreatedException(String message) {
        super(message);
    }
}
