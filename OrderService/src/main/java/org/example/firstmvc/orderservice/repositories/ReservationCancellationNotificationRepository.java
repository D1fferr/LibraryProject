package org.example.firstmvc.orderservice.repositories;

import org.example.firstmvc.orderservice.dto.JoinDTOForCancelledReservations;
import org.example.firstmvc.orderservice.models.ReservationCancellationNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ReservationCancellationNotificationRepository extends JpaRepository<ReservationCancellationNotification, UUID> {

    @Query("SELECT NEW org.example.firstmvc.orderservice.dto.JoinDTOForCancelledReservations(" +
            "e1.reservationDate, e1.reservationBook, e1.reservationStatus, " +
            "e2.message) " +
            "FROM Reservation e1 LEFT JOIN ReservationCancellationNotification e2 ON e1.reservationId = e2.reservationId")
    List<JoinDTOForCancelledReservations> findReservations(UUID id);

}
