package com.library.FrontendMicroservice.models;

import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Future(message = "You cannot select the booking date to be in the past or today.")
    private LocalDate reservationDate;
    private UUID reservationBook;
    private UUID reservationUser;
    private ReservationStatus reservationStatus;
}