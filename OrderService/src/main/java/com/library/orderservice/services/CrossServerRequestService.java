package com.library.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.library.orderservice.dto.ReservationDTO;
import com.library.orderservice.util.BookNotFoundException;
import com.library.orderservice.util.ReservationNotAllowedException;
import org.springframework.data.domain.PageRequest;
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
        if (reservationService.findReservationByBookId(reservationDTO.getReservationDate(), reservationDTO.getReservationBook())>booksAvailable){
            log.warn("Reservations for the selected date are not available for user: '{}'", reservationDTO.getReservationUser());
            throw new ReservationNotAllowedException("Reservations for the selected date are not available.");
        }
        log.info("There is available date for user: '{}'", reservationDTO.getReservationUser());
        log.info("Checking available items for user: '{}'", reservationDTO.getReservationUser());
        if (reservationService.findReservationUserId(reservationDTO.getReservationDate(), reservationDTO.getReservationUser())>5){
            log.warn("You cannot reserve more than 5 books in 1 day. User: '{}'", reservationDTO.getReservationUser());
            throw new ReservationNotAllowedException("You cannot reserve more than 5 books in 1 day. ");
        }
        log.info("There is available items for user: '{}'", reservationDTO.getReservationUser());
    }
    public Integer booksAvailable(UUID bookId) {
        try {
            log.info("Trying to connect to book service to get available items");
            Integer items = restTemplate.getForObject(
                    "http://localhost:8081/book/public/pieces/{book_id}",
                    Integer.class,
                    bookId
            );
            log.info("The connection is successful");
            Integer orderedBooks = reservationService.findReservationByBookId(bookId);
            int availableBooks = 0;
            if (items!=null)
                availableBooks = items - orderedBooks;
            return availableBooks;

        } catch (Exception e) {
            log.warn("The connection failed. Error: '{}'", e.getMessage());
            throw new BookNotFoundException("Book not found");
        }

    }

}
