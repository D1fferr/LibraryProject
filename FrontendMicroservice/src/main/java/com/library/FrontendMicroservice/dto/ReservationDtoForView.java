package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
public class ReservationDtoForView {
    private UUID id;
    private UUID bookId;
    private String bookName;
    private String bookAuthor;
    private String bookImage;
    private LocalDate reservationDate;
    private ReservationStatus status;
}
