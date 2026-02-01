package com.library.orderservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor
public class Reservation {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "reservation_id")
    private UUID reservationId;
    @Column(name = "reservation_date")
    @Future(message = "You cannot select the booking date to be in the past or today.")
    private LocalDate reservationDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull(message = "You must select an existing book.")
    @Column(name = "reservation_book_id")
    private UUID reservationBook;
    @NotNull(message = "You must select an existing account.")
    @Column(name = "reservation_person_id")
    private UUID reservationUser;
    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

}