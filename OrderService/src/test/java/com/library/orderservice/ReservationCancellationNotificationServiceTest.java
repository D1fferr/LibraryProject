package com.library.orderservice;

import com.library.orderservice.dto.JoinDTOForCancelledReservations;
import com.library.orderservice.dto.ReservationCancellationNotificationDTO;
import com.library.orderservice.models.ReservationCancellationNotification;
import com.library.orderservice.repositories.ReservationCancellationNotificationRepository;
import com.library.orderservice.services.ReservationCancellationNotificationService;
import com.library.orderservice.services.ReservationService;
import com.library.orderservice.util.ReservationNotFoundException;
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
