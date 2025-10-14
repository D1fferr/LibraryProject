package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.zakharchuk.ExpectedBooksService.exceptions.EmailSendingException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final ReportAvailabilityService reportAvailabilityService;
    private String sendFrom = "";
    private String emailSubject = "";


    public void send(UUID id) {

        List<ReportAvailability> reportAvailabilityList = reportAvailabilityService.findAllByBookId(id);

        for (ReportAvailability reportAvailability : reportAvailabilityList) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(sendFrom);
                message.setTo(reportAvailability.getUserEmail());
                message.setSubject(emailSubject);
                message.setText(textPrep(reportAvailability.getUsername(),
                        reportAvailability.getId().toString()));
                reportAvailabilityService.changeStatus(reportAvailability.getId());
            } catch (Exception e) {

                throw new EmailSendingException("Failed to send email");
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
    }
