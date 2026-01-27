package org.example.firstmvc.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.firstmvc.orderservice.dto.ReservationCancellationNotificationDTO;
import org.example.firstmvc.orderservice.dto.UserDTO;
import org.example.firstmvc.orderservice.models.Reservation;
import org.example.firstmvc.orderservice.util.EmailSendingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {


    @Value("${send.message.from}")
    private String from;
    private final RestTemplate restTemplate;
    private final ReservationService reservationService;
    private final JavaMailSender mailSender;


    public void send(ReservationCancellationNotificationDTO dto, UUID id) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            log.info("Trying to send message to email. User id: '{}'", id);
            UserDTO user = getUser(id);
            mailMessage.setFrom(from);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Cancellation of reservation");
            mailMessage.setText(textPrep(user.getUsername(), dto.getMessage()));
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.warn("Failed to send message to email. User id '{}', Errors: '{}'", id, e.getMessage());
            throw new EmailSendingException(e.getMessage());
        }

    }
    private UserDTO getUser (UUID id) throws Exception {
            Reservation reservation = reservationService.findByReservationId(id);
            String url = "http://localhost:8087/user/" + reservation.getReservationUser();
            return restTemplate.getForObject(url, UserDTO.class);
    }
    private String textPrep(String username, String message) {
        return "Hello dear " + username +
                "." + " We regret to inform you that your book reservation has been canceled due to: " + message +
                " We will be glad to see you again";

    }

}
