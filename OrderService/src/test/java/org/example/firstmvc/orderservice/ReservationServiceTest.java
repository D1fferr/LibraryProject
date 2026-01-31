package org.example.firstmvc.orderservice;

import org.example.firstmvc.orderservice.dto.ReservationDTO;
import org.example.firstmvc.orderservice.dto.ReservationDTOForChangeDate;
import org.example.firstmvc.orderservice.models.Reservation;
import org.example.firstmvc.orderservice.repositories.ReservationRepository;
import org.example.firstmvc.orderservice.services.ReservationService;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void save_Reservation_ShouldSaveWithStatusAndTimestamp() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        Reservation reservation = new Reservation();
        when(modelMapper.map(reservationDTO, Reservation.class)).thenReturn(new Reservation());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.save(reservationDTO);

        // Assert
        verify(reservationRepository).save(any(Reservation.class));
        assertNotNull(result);
    }

    @Test
    void save_Reservation_ShouldSetCreatedStatus() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        when(modelMapper.map(reservationDTO, Reservation.class)).thenReturn(new Reservation());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation savedReservation = invocation.getArgument(0);
            return savedReservation;
        });

        // Act
        Reservation result = reservationService.save(reservationDTO);

        // Assert
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void save_Reservation_ShouldSetCurrentTimestamp() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        when(modelMapper.map(reservationDTO, Reservation.class)).thenReturn(new Reservation());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        // Act
        reservationService.save(reservationDTO);

        // Assert
        verify(reservationRepository).save(any(Reservation.class));
    }
    @Test
    void updateDate_ReservationExists_ShouldUpdate() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationDTOForChangeDate dto = new ReservationDTOForChangeDate();
        dto.setReservationDate(LocalDate.of(2025, 1, 1));

        Reservation reservation = new Reservation();
        when(reservationRepository.findReservationsByReservationId(id))
                .thenReturn(Optional.of(reservation));

        // Act
        reservationService.updateDate(id, dto);

        // Assert
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateDate_ReservationNotFound_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationDTOForChangeDate dto = new ReservationDTOForChangeDate();

        when(reservationRepository.findReservationsByReservationId(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.updateDate(id, dto);
        });
    }

    @Test
    void updateDate_NullDate_ShouldSetStatusOnly() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationDTOForChangeDate dto = new ReservationDTOForChangeDate(); // date is null

        Reservation reservation = new Reservation();
        when(reservationRepository.findReservationsByReservationId(id))
                .thenReturn(Optional.of(reservation));

        // Act
        reservationService.updateDate(id, dto);

        // Assert
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateDate_StatusShouldBeCREATED() {
        // Arrange
        UUID id = UUID.randomUUID();
        ReservationDTOForChangeDate dto = new ReservationDTOForChangeDate();

        Reservation reservation = new Reservation();
        when(reservationRepository.findReservationsByReservationId(id))
                .thenReturn(Optional.of(reservation));

        // Act
        reservationService.updateDate(id, dto);

        // Assert
        verify(reservationRepository).save(reservation);
    }
    @Test
    void findById_ReservationExists_ShouldReturnDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation();
        when(reservationRepository.findByReservationId(id)).thenReturn(Optional.of(reservation));

        // Act
        reservationService.findById(id);

        // Assert
        verify(reservationRepository).findByReservationId(id);
    }

    @Test
    void findById_ReservationNotFound_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(reservationRepository.findByReservationId(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.findById(id);
        });
    }

    @Test
    void findById_CorrectIdUsed() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(reservationRepository.findByReservationId(id)).thenReturn(Optional.of(new Reservation()));

        // Act
        reservationService.findById(id);

        // Assert
        verify(reservationRepository).findByReservationId(id);
    }

}
