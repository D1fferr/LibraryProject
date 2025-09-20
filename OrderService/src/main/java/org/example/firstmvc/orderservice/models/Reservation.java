package org.example.firstmvc.orderservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation")
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
    @Positive(message = "You must select an existing book.")
    @Column(name = "reservation_book_id")
    private UUID reservationBook;
    @Positive(message = "You must select an existing account.")
    @Column(name = "reservation_person_id")
    private UUID reservationUser;
    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public Reservation(LocalDate reservationDate, LocalDateTime createdAt, UUID reservationBook, UUID reservationUser, ReservationStatus reservationStatus) {
        this.reservationDate = reservationDate;
        this.createdAt = createdAt;
        this.reservationBook = reservationBook;
        this.reservationUser = reservationUser;
        this.reservationStatus = reservationStatus;
    }
    public Reservation(){}

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getReservationBook() {
        return reservationBook;
    }

    public void setReservationBook(UUID reservationBook) {
        this.reservationBook = reservationBook;
    }

    public UUID getReservationUser() {
        return reservationUser;
    }

    public void setReservationUser(UUID reservationUser) {
        this.reservationUser = reservationUser;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}