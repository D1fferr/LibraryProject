package com.library.orderservice.repositories;


import com.library.orderservice.models.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    Optional<Reservation> findByReservationId(UUID reservationId);
    List<Reservation> findReservationByReservationBook(UUID reservationBook, Pageable pageable);
    List<Reservation> findReservationByReservationUser(UUID reservationUser, Pageable pageable);

    Optional<Reservation> findReservationsByReservationId(UUID reservationId);
    List<Reservation> findReservationByReservationDateAndReservationBook(LocalDate reservationDate, UUID reservationBook);
}
