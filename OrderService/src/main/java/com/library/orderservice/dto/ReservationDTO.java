package com.library.orderservice.dto;

import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.library.orderservice.models.ReservationStatus;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private LocalDate reservationDate;
    private UUID reservationBook;
    private UUID reservationUser;
    private ReservationStatus reservationStatus;
}
