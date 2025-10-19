package ua.zakharchuk.ExpectedBooksService.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;


import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetryEmailService {


    private final JavaMailSender javaMailSender;
    private final ReportAvailabilityErrorService errorService;

    public void send(){
        System.out.println("222222");
        List<ReportAvailabilityError> reportAvailabilityList = errorService.findAllByStatus(Status.CREATED);

        for (ReportAvailabilityError reportAvailability : reportAvailabilityList) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("***");
                message.setTo(reportAvailability.getUserEmail());
                message.setSubject("Last update of books");
                message.setText(textPrep(reportAvailability.getUsername(),
                        reportAvailability.getId().toString()));
                javaMailSender.send(message);
                errorService.updateStatus(reportAvailability.getId());
            } catch (Exception e) {
                System.out.println("1111");
                errorService.updateStatusWithError(reportAvailability.getId(), e.getMessage());
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