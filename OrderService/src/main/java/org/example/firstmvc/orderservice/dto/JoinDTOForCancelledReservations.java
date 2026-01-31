package org.example.firstmvc.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.firstmvc.orderservice.models.ReservationStatus;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class JoinDTOForCancelledReservations {

    private LocalDate reservationDate;
    private UUID reservationBook;
    private ReservationStatus reservationStatus;
    private String message;
}
