//package ua.zakharchuk.ExpectedBooksService.services;
//
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.retry.support.RetryTemplate;
//import org.springframework.stereotype.Service;
//import ua.zakharchuk.ExpectedBooksService.exceptions.EmailSendingException;
//import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;
//import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
//import ua.zakharchuk.ExpectedBooksService.models.Status;
//
//
//import javax.swing.text.html.parser.Entity;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RetryEmailService {
//
//
//    private final JavaMailSender javaMailSender;
//    private final ReportAvailabilityErrorService errorService;
//    private final RetryTemplate retryTemplate;
//
//    public void retrySendingEmail(){
//        List<ReportAvailabilityError> reportAvailabilityList = errorService.findAllByStatus(Status.CREATED);
//        for (ReportAvailabilityError reportAvailability : reportAvailabilityList) {
//            retryTemplate.execute(context->{
//                send(reportAvailability);
//                return null;
//            });
//        }
//    }
//
//    public void send(ReportAvailabilityError reportAvailabilityError){
//        System.out.println("222222");
//            try {
//                SimpleMailMessage message = new SimpleMailMessage();
//                message.setFrom("***");
//                message.setTo(reportAvailabilityError.getUserEmail());
//                message.setSubject("Last update of books");
//                message.setText(textPrep(reportAvailabilityError.getUsername(),
//                        reportAvailabilityError.getId().toString()));
//                javaMailSender.send(message);
//                System.out.println("1/2");
//                errorService.updateStatus(reportAvailabilityError.getId());
//            } catch (Exception e) {
//                System.out.println("1111");
//                errorService.updateStatusWithError(reportAvailabilityError.getId(), e.getMessage());
//                throw new EmailSendingException(e.getMessage());
//            }
//
//    }
//    public String textPrep (String username, String id){
//        return "Hello dear " + username +
//                "." + " The book you wanted to review is now available. You can reserve it at the link:" +
//                "http://localhost:8081/" +
//                id +
//                " We will be glad to see you again";
//
//    }
//    }