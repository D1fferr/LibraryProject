package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
public class ReservationForViewForReservationPage {
    private UUID id;                    // ID резервації
    private String bookName;            // Назва книги
    private String bookAuthor;          // Автор книги
    private String username;            // Ім'я користувача
    private LocalDate reservationDate;  // Дата резервації
    private ReservationStatus status;              // Статус (CREATED, CONFIRMED, CANCELLED
}
