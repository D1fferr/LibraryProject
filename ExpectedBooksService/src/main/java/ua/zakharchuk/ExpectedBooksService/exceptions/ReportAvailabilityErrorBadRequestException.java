package ua.zakharchuk.ExpectedBooksService.exceptions;

public class ReportAvailabilityErrorBadRequestException extends RuntimeException {
    public ReportAvailabilityErrorBadRequestException(String message) {
        super(message);
    }
}
