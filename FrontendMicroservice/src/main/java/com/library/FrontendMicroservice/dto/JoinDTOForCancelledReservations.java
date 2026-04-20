package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JoinDTOForCancelledReservations {

    private LocalDate reservationDate;
    private UUID reservationBook;
    private ReservationStatus reservationStatus;
    private String message;
}
