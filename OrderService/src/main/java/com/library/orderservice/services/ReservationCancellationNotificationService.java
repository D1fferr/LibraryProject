package com.library.orderservice.services;

import com.library.orderservice.dto.ReservationDTO;
import com.library.orderservice.models.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.library.orderservice.dto.JoinDTOForCancelledReservations;
import com.library.orderservice.dto.ReservationCancellationNotificationDTO;
import com.library.orderservice.models.ReservationCancellationNotification;
import com.library.orderservice.repositories.ReservationCancellationNotificationRepository;
import com.library.orderservice.util.ReservationNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
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
            log.warn("Reservations not found");
            throw new ReservationNotFoundException("Reservations not found");
        }
        log.info("All canceled reservations found");
        return reservations;
    }
    @Transactional(readOnly = true)
    public JoinDTOForCancelledReservations findOneForUser(UUID id){
        Optional<ReservationCancellationNotification> optionalReservation = repository.findByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.warn("The reservation by id not found. ID: '{}'", id);
            throw new ReservationNotFoundException("The reservation not found");
        }
        log.info("The reservation by id found. ID: '{}'", id);
        JoinDTOForCancelledReservations dto = new JoinDTOForCancelledReservations();
        ReservationDTO reservationDTO = reservationService.findById(optionalReservation.get().getReservationId());
        dto.setReservationBook(reservationDTO.getReservationBook());
        dto.setReservationDate(reservationDTO.getReservationDate());
        dto.setReservationStatus(reservationDTO.getReservationStatus());
        dto.setMessage(optionalReservation.get().getMessage());
        return dto;
    }

    private ReservationCancellationNotification toEntity(ReservationCancellationNotificationDTO dto){
        log.info("Mapping the reservation cancellation notificationDTO to entity");
        return modelMapper.map(dto, ReservationCancellationNotification.class);
    }

}
