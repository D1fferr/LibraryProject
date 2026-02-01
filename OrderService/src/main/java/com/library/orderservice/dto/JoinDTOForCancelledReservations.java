package com.library.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.library.orderservice.models.ReservationStatus;

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
