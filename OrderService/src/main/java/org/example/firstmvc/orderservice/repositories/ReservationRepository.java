package org.example.firstmvc.orderservice.repositories;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import org.example.firstmvc.orderservice.models.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Optional<Reservation> findByReservationId(UUID reservationId);
    List<Reservation> findReservationByReservationBook(UUID reservationBook, Pageable pageable);
    List<Reservation> findReservationByReservationUser(UUID reservationUser, Pageable pageable);

    Optional<Reservation> findReservationsByReservationId(UUID reservationId);
    List<Reservation> findReservationByReservationDateAndReservationBook(LocalDate reservationDate, UUID reservationBook);
}
