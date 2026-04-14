package com.library.orderservice.dto;


import com.library.orderservice.models.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReservationForView {
    private UUID reservationId;
    private LocalDate reservationDate;
    private UUID reservationBook;
    private UUID reservationUser;
    private ReservationStatus reservationStatus;
}
