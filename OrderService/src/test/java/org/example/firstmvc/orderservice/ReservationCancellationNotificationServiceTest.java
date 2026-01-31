package org.example.firstmvc.orderservice;

import org.example.firstmvc.orderservice.dto.JoinDTOForCancelledReservations;
import org.example.firstmvc.orderservice.dto.ReservationCancellationNotificationDTO;
import org.example.firstmvc.orderservice.models.ReservationCancellationNotification;
import org.example.firstmvc.orderservice.repositories.ReservationCancellationNotificationRepository;
import org.example.firstmvc.orderservice.services.ReservationCancellationNotificationService;
import org.example.firstmvc.orderservice.services.ReservationService;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationCancellationNotificationServiceTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationCancellationNotificationRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReservationCancellationNotificationService notificationService;

    @Test
    void save_ShouldChangeStatusAndSaveNotification() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationCancellationNotificationDTO dto = new ReservationCancellationNotificationDTO();
        dto.setReservationId(id);
        dto.setMessage("Test");
        when(modelMapper.map(dto, ReservationCancellationNotification.class)).thenReturn(new ReservationCancellationNotification());

        // Act
        notificationService.save(dto, id);

        // Assert
        verify(reservationService).changeStatusToCanceled(id);
        verify(repository).save(any(ReservationCancellationNotification.class));
    }

    @Test
    void save_ShouldCallBothMethods() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationCancellationNotificationDTO dto = new ReservationCancellationNotificationDTO();
        when(modelMapper.map(dto, ReservationCancellationNotification.class)).thenReturn(new ReservationCancellationNotification());

        // Act
        notificationService.save(dto, id);

        // Assert
        verify(reservationService).changeStatusToCanceled(id);
        verify(repository).save(any());
    }

    @Test
    void save_VerifyCorrectIdUsed() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationCancellationNotificationDTO dto = new ReservationCancellationNotificationDTO();
        when(modelMapper.map(dto, ReservationCancellationNotification.class)).thenReturn(new ReservationCancellationNotification());

        // Act
        notificationService.save(dto, id);

        // Assert
        verify(reservationService).changeStatusToCanceled(id);
    }
    @Test
    void findAllReservationsForUser_ReservationsExist_ShouldReturnList() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<JoinDTOForCancelledReservations> reservations = List.of(
                new JoinDTOForCancelledReservations(),
                new JoinDTOForCancelledReservations()
        );

        when(repository.findReservations(id)).thenReturn(reservations);

        // Act
        List<JoinDTOForCancelledReservations> result = notificationService.findAllReservationsForUser(id);

        // Assert
        verify(repository).findReservations(id);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllReservationsForUser_NoReservations_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findReservations(id)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> {
            notificationService.findAllReservationsForUser(id);
        });
    }

    @Test
    void findAllReservationsForUser_CorrectIdUsed() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findReservations(id)).thenReturn(List.of(new JoinDTOForCancelledReservations()));

        // Act
        notificationService.findAllReservationsForUser(id);

        // Assert
        verify(repository).findReservations(id);
    }


}
