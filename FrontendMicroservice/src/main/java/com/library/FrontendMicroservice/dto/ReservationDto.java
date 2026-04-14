package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
public class ReservationDto {
    private UUID reservationId;
    private LocalDate reservationDate;
    private UUID reservationBook;
    private UUID reservationUser;
    private ReservationStatus reservationStatus;
}
