package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReservationForView {
    private UUID id;
    private String username;
    private LocalDate reservationDate;
    private ReservationStatus status;

}
