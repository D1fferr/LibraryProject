package org.example.firstmvc.orderservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import org.example.firstmvc.orderservice.models.ReservationStatus;

import java.time.LocalDate;
import java.util.UUID;


public class ReservationDTO {
    @Future(message = "You cannot select the booking date to be in the past or today.")
    private LocalDate reservationDate;
    private UUID reservationBook;
    private UUID reservationUser;
    private ReservationStatus reservationStatus;

    public ReservationDTO(LocalDate reservationDate, UUID reservationBook, UUID reservationUser, ReservationStatus reservationStatus) {
        this.reservationDate = reservationDate;
        this.reservationBook = reservationBook;
        this.reservationUser = reservationUser;
        this.reservationStatus = reservationStatus;
    }
    public ReservationDTO(){}

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
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

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationUser(UUID reservationUser) {
        this.reservationUser = reservationUser;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
