package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
public class ReservationForViewForAdmin {
    private UUID id;
    private String bookName;
    private String bookAuthor;
    private LocalDate reservationDate;
    private ReservationStatus status;

    public boolean isExpired() {
        return reservationDate != null && reservationDate.isBefore(LocalDate.now());
    }
}
