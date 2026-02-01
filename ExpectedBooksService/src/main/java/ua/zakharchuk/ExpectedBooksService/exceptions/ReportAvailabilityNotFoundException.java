package ua.zakharchuk.ExpectedBooksService.exceptions;

public class ReportAvailabilityNotFoundException extends RuntimeException {
    public ReportAvailabilityNotFoundException(String message) {
        super(message);
    }
}
