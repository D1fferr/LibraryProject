package org.example.firstmvc.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.firstmvc.orderservice.dto.ReservationDTO;
import org.example.firstmvc.orderservice.dto.ReservationDTOForChangeDate;
import org.example.firstmvc.orderservice.models.Reservation;
import org.example.firstmvc.orderservice.models.ReservationStatus;
import org.example.firstmvc.orderservice.repositories.ReservationRepository;
import org.example.firstmvc.orderservice.util.BookNotFoundException;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.example.firstmvc.orderservice.util.ReservationsNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final RestTemplate restTemplate;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final KafkaSenderService kafkaSenderService;

    @Transactional
    public Reservation save(ReservationDTO reservationDTO) {
        log.info("Trying to save reservation");
        Reservation reservation = toEntity(reservationDTO);
        reservation.setReservationStatus(ReservationStatus.CREATED);
        reservation.setCreatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        log.info("Reservation saved. ID: '{}'", reservation.getReservationId());
        return reservation;
    }

    @Transactional
    public void updateDate(UUID id, ReservationDTOForChangeDate reservationDTO) {
        log.info("Trying to find the reservations to change reservations date. ID: '{}'", id);
        Optional<Reservation> optionalReservation = reservationRepository.findReservationsByReservationId(id);
        if (optionalReservation.isEmpty()){
            log.info("No reservations to change reservations date. ID: '{}'", id);
            throw new ReservationNotFoundException("No reservations found");
        }

        Reservation reservation = optionalReservation.get();
        if (reservationDTO.getReservationDate() != null)
            reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setReservationStatus(ReservationStatus.CREATED);
        reservationRepository.save(reservation);
        log.info("The reservations date changed. ID: '{}'", id);

    }

    @Transactional
    public void updateStatusToConfirmed(UUID id) {
        log.info("Trying to find the reservations to change status to confirmed. ID: '{}'", id);
        Optional<Reservation> optionalReservation = reservationRepository.findReservationsByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.info("No reservations to change status to confirmed found. ID: '{}'", id);
            throw new ReservationNotFoundException("No reservations found");
        }
        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        log.info("The reservations status changed to confirmed. ID: '{}'", id);
    }

    @Transactional
    public void changeStatusToCanceled(UUID id) {
        log.info("Trying to find the reservations to change status to canceled. ID: '{}'", id);
        Optional<Reservation> optionalReservation = reservationRepository.findReservationsByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.info("No reservations to change status to canceled found. ID: '{}'", id);
            throw new ReservationNotFoundException("No reservations found");
        }
        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        log.info("The reservations status changed to canceled. ID: '{}'", id);
    }

    @Transactional
    public void delete(UUID id) {
        reservationRepository.deleteById(id);
        log.info("The reservations deleted. ID: '{}'", id);
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByBookId(LocalDate localDate, UUID reservationBook) {
        List<Reservation> reservationList = reservationRepository.findReservationByReservationDateAndReservationBook(localDate, reservationBook);
        if (reservationList.isEmpty()) {
            log.info("No reservations for check available items found");
            throw new ReservationsNotFoundException("No reservations found");
        }
        return reservationList.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByBookId(UUID reservationBook, PageRequest pageRequest) {
        log.info("Trying to find the reservations by book id: '{}'", reservationBook);
        List<Reservation> reservationList = reservationRepository.findReservationByReservationBook(reservationBook, pageRequest);
        if (reservationList.isEmpty()) {
            log.info("No reservations by book id found '{}'", reservationBook);
            throw new ReservationsNotFoundException("No reservations found");
        }
        log.info("All reservations by book id found '{}'", reservationBook);
        return reservationList.stream().map(this::toDTO).toList();

    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationUserId(LocalDate localDate, UUID reservationUser) {
        List<Reservation> reservationList = reservationRepository.findReservationByReservationDateAndReservationBook(localDate, reservationUser);
        if (reservationList.isEmpty()) {
            log.info("No reservations for check available date found");
            throw new ReservationsNotFoundException("No reservations found");
        }
        return reservationList.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByUserId(UUID reservationUser, PageRequest pageRequest) {
        log.info("Trying to find the reservations by user id: '{}'", reservationUser);
        List<Reservation> reservationList = reservationRepository.findReservationByReservationUser(reservationUser, pageRequest);
        if (reservationList.isEmpty()) {
            log.info("No reservations by user id found '{}'", reservationUser);
            throw new ReservationsNotFoundException("No reservations found");
        }
        log.info("All reservations by user id found '{}'", reservationUser);
        return reservationList.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findAllReservation(PageRequest pageRequest) {
        log.info("Trying to find all reservations");
        Page<Reservation> reservationList = reservationRepository.findAll(pageRequest);
        if (reservationList.isEmpty()) {
            log.info("No reservations found");
            throw new ReservationsNotFoundException("No reservations found");
        }
        log.info("All reservations found");
        return reservationList.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ReservationDTO findById(UUID id) {
        log.info("Trying to find the reservation by id. ID: '{}'", id);
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.info("The reservation by id not found. ID: '{}'", id);
            throw new ReservationNotFoundException("The reservation not found");
        }
        log.info("The reservation by id found. ID: '{}'", id);
        return toDTO(optionalReservation.get());
    }

    @Transactional(readOnly = true)
    public Reservation findByReservationId(UUID id) {
        log.info("Trying to find the reservation. ID: '{}'", id);
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.info("The reservation not found. ID: '{}'", id);
            throw new ReservationNotFoundException("The reservation not found");
        }
        log.info("The reservation found. ID: '{}'", id);
        return optionalReservation.get();
    }

    public Integer booksAvailable(UUID bookId) {
        try {
            log.info("Trying to connect to book service to get available items");
            Integer items = restTemplate.getForObject(
                    "http://localhost:8081/book/pieces/{book_id}",
                    Integer.class,
                    bookId
            );
            log.info("The connection is successful");
            return items;

        } catch (Exception e) {
            log.info("The connection failed. Error: '{}'", e.getMessage());
            throw new BookNotFoundException("Book not found");
        }

    }


    private ReservationDTO toDTO(Reservation reservation) {
        log.info("Mapping reservation entity to dto for response. ID: '{}'", reservation.getReservationId());
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    private Reservation toEntity(ReservationDTO reservationDTO) {
        log.info("Mapping reservationDTO to entity. User: '{}'", reservationDTO.getReservationBook());
        return modelMapper.map(reservationDTO, Reservation.class);
    }
}
