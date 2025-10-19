package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.exceptions.EmailSendingException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityErrorRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final ReportAvailabilityService reportAvailabilityService;
    private final ReportAvailabilityErrorService errorService;


    public void send(UUID id) {

        List<ReportAvailability> reportAvailabilityList = reportAvailabilityService.findAllByBookId(id);
        System.out.println("Finding books");
        for (ReportAvailability reportAvailability : reportAvailabilityList) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("z****");
                message.setTo(reportAvailability.getUserEmail());
                message.setSubject("Last update of books");
                message.setText(textPrep(reportAvailability.getUsername(),
                        reportAvailability.getId().toString()));
                mailSender.send(message);
                System.out.println("Message sent" + reportAvailability.getUserEmail());
                reportAvailabilityService.changeStatus(reportAvailability.getId());
            } catch (Exception e) {
                System.out.println("Error sent message");
                ReportAvailabilityError reportAvailabilityError = changeTypeToReportAvailabilityError(reportAvailability, e);
                System.out.println("To error");
                errorService.save(reportAvailabilityError);
                System.out.println("saved");
                throw new EmailSendingException(e.getMessage());
            }
        }

    }
        public String textPrep (String username, String id){
            return "Hello dear " + username +
                    "." + " The book you wanted to review is now available. You can reserve it at the link:" +
                    "http://localhost:8081/" +
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
