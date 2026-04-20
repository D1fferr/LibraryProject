package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import ua.zakharchuk.ExpectedBooksService.exceptions.EmailSendingException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {
    @Value("${send.message.from}")
    private String from;

    private final JavaMailSender mailSender;
    private final ReportAvailabilityService reportAvailabilityService;
    private final ReportAvailabilityErrorService errorService;
    private final RetryTemplate retryTemplate;

    private void retrySendingEmail(List<ReportAvailability> reportAvailabilityList) {
        for (ReportAvailability reportAvailability : reportAvailabilityList) {
            retryTemplate.execute(context -> {
                try {

                    sendEmail(reportAvailability);
                    return null;
                } catch (Exception e) {
                    if (context.getRetryCount() >= 2) {

                        ReportAvailabilityError reportAvailabilityError = changeTypeToReportAvailabilityError(reportAvailability, e);
                        errorService.save(reportAvailabilityError);
                        log.info("Saved message with errors. ID: '{}'", reportAvailabilityError.getId());
                    }
                    log.warn("Failed to sent message. ID: '{}', Errors: '{}'", reportAvailability.getId(), e.getMessage());
                    throw new EmailSendingException(e.getMessage());
                }
            });
        }
    }

    public void send(UUID id) {
        List<ReportAvailability> reportAvailabilityList = reportAvailabilityService.findAllByBookId(id);
        List<ReportAvailability> listWithErrors = new ArrayList<>();
        for (ReportAvailability reportAvailability : reportAvailabilityList) {
            try {
                sendEmail(reportAvailability);
            } catch (Exception e) {
                listWithErrors.add(reportAvailability);
            }
        }
        if (!listWithErrors.isEmpty())
            retrySendingEmail(listWithErrors);
    }

    private void sendEmail(ReportAvailability reportAvailability) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(reportAvailability.getUserEmail());
            message.setSubject("Last update of books");
            message.setText(textPrep(reportAvailability.getUsername(),
                    reportAvailability.getExpectedBookId().toString()));
            log.info("Trying to send message to email. Email: '{}'", reportAvailability.getUserEmail());
            mailSender.send(message);
            log.info("Message send. Email: '{}'", reportAvailability.getUserEmail());
            reportAvailabilityService.changeStatus(reportAvailability.getId());
        } catch (Exception e) {
            log.warn("Failed to sent message. Email: '{}', Error: '{}'", reportAvailability.getUserEmail(), e.getMessage());
            throw new EmailSendingException(e.getMessage());
        }

    }

    private String textPrep(String username, String id) {
        return "Hello dear " + username +
                "." + " The book you wanted to review is now available. You can reserve it at the link:" +
                "http://localhost:8080/book/" +
                id +
                " We will be glad to see you again";

    }

    private ReportAvailabilityError changeTypeToReportAvailabilityError(ReportAvailability reportAvailability, Exception e) {
        ReportAvailabilityError reportAvailabilityError = new ReportAvailabilityError();
        reportAvailabilityError.setUserEmail(reportAvailability.getUserEmail());
        reportAvailabilityError.setUsername(reportAvailability.getUsername());
        reportAvailabilityError.setStatus(reportAvailability.getStatus());
        reportAvailabilityError.setExpectedBookId(reportAvailability.getExpectedBookId());
        reportAvailabilityError.setError(e.getMessage());
        reportAvailabilityError.setUserId(reportAvailability.getUserId());
        return reportAvailabilityError;
    }
}
