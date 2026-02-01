package ua.zakharchuk.ExpectedBooksService.exceptions;

public class FailedSaveImageException extends RuntimeException {
    public FailedSaveImageException(String message) {
        super(message);
    }
}
