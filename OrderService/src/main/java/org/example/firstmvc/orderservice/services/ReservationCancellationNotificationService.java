package org.example.firstmvc.orderservice.services;

import lombok.RequiredArgsConstructor;
import org.example.firstmvc.orderservice.dto.JoinDTOForCancelledReservations;
import org.example.firstmvc.orderservice.dto.ReservationCancellationNotificationDTO;
import org.example.firstmvc.orderservice.models.Reservation;
import org.example.firstmvc.orderservice.models.ReservationCancellationNotification;
import org.example.firstmvc.orderservice.repositories.ReservationCancellationNotificationRepository;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    }
    @Transactional(readOnly = true)
    public List<JoinDTOForCancelledReservations> findAllReservationsForUser(UUID id){
        List<JoinDTOForCancelledReservations> reservations = repository.findReservations(id);
        if (reservations.isEmpty())
            throw new ReservationNotFoundException("Reservations not found");
        return reservations;
    }

    private ReservationCancellationNotification toEntity(ReservationCancellationNotificationDTO dto){
        return modelMapper.map(dto, ReservationCancellationNotification.class);
    }
    private ReservationCancellationNotificationDTO toDTO(ReservationCancellationNotification entity){
        return modelMapper.map(entity, ReservationCancellationNotificationDTO.class);
    }

}
