package org.example.firstmvc.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.firstmvc.orderservice.dto.ReservationDTO;
import org.example.firstmvc.orderservice.util.BookNotFoundException;
import org.example.firstmvc.orderservice.util.ReservationNotAllowedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrossServerRequestService {
    private final ReservationService reservationService;
    private final RestTemplate restTemplate;

    public void checkAvailableDate(ReservationDTO reservationDTO){
        int booksAvailable = booksAvailable(reservationDTO.getReservationBook());
        log.info("Checking available date for user: '{}'", reservationDTO.getReservationUser());
        if (reservationService.findReservationByBookId(reservationDTO.getReservationDate(), reservationDTO.getReservationBook()).size()>booksAvailable){
            log.warn("Reservations for the selected date are not available for user: '{}'", reservationDTO.getReservationUser());
            throw new ReservationNotAllowedException("Reservations for the selected date are not available.");
        }
        log.info("There is available date for user: '{}'", reservationDTO.getReservationUser());
        log.info("Checking available items for user: '{}'", reservationDTO.getReservationUser());
        if (reservationService.findReservationUserId(reservationDTO.getReservationDate(), reservationDTO.getReservationUser()).size()>5){
            log.warn("You cannot reserve more than 5 books in 1 day. User: '{}'", reservationDTO.getReservationUser());
            throw new ReservationNotAllowedException("You cannot reserve more than 5 books in 1 day. ");
        }
        log.info("There is available items for user: '{}'", reservationDTO.getReservationUser());
    }
    public Integer booksAvailable(UUID bookId) {
        try {
            log.info("Trying to connect to book service to get available items");
            Integer items = restTemplate.getForObject(
                    "http://localhost:8081/book/pieces/{book_id}",
                    Integer.class,
                    bookId
            );
            log.info("The connection is successful");
            return items;

        } catch (Exception e) {
            log.warn("The connection failed. Error: '{}'", e.getMessage());
            throw new BookNotFoundException("Book not found");
        }

    }

}
