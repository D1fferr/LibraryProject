package org.example.firstmvc.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.firstmvc.orderservice.dto.JoinDTOForCancelledReservations;
import org.example.firstmvc.orderservice.dto.ReservationCancellationNotificationDTO;
import org.example.firstmvc.orderservice.models.ReservationCancellationNotification;
import org.example.firstmvc.orderservice.repositories.ReservationCancellationNotificationRepository;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationCancellationNotificationService {

    private final ReservationCancellationNotificationRepository repository;
    private final ModelMapper modelMapper;
    private final ReservationService reservationService;

    @Transactional
    public void save(ReservationCancellationNotificationDTO dto, UUID id){

        reservationService.changeStatusToCanceled(id);
        ReservationCancellationNotification reservationCancellationNotification = toEntity(dto);
        repository.save(reservationCancellationNotification);
        log.info("The reservation cancellation notification saved. ID: '{}'", reservationCancellationNotification.getReservationId());

    }
    @Transactional(readOnly = true)
    public List<JoinDTOForCancelledReservations> findAllReservationsForUser(UUID id){
        log.info("Trying to find all canceled reservations");
        List<JoinDTOForCancelledReservations> reservations = repository.findReservations(id);
        if (reservations.isEmpty()) {
            log.info("Reservations not found");
            throw new ReservationNotFoundException("Reservations not found");
        }
        log.info("All canceled reservations found");
        return reservations;
    }

    private ReservationCancellationNotification toEntity(ReservationCancellationNotificationDTO dto){
        log.info("Mapping the reservation cancellation notificationDTO to entity");
        return modelMapper.map(dto, ReservationCancellationNotification.class);
    }

}
