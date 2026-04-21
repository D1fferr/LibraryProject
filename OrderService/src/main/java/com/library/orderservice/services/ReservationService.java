package com.library.orderservice.services;

import com.library.orderservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.library.orderservice.models.Reservation;
import com.library.orderservice.models.ReservationStatus;
import com.library.orderservice.repositories.ReservationRepository;
import com.library.orderservice.util.ReservationNotFoundException;
import com.library.orderservice.util.ReservationsNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Reservation save(ReservationDTO reservationDTO) {
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
            log.warn("No reservations to change reservations date. ID: '{}'", id);
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
        Optional<Reservation> optionalReservation = reservationRepository.findReservationsByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.warn("No reservations to change status to confirmed found. ID: '{}'", id);
            throw new ReservationNotFoundException("No reservations found");
        }
        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        log.info("The reservations status changed to confirmed. ID: '{}'", id);
    }

    @Transactional
    public void changeStatusToCanceled(UUID id) {
        log.info("Trying to find the reservation to change status to canceled. ID: '{}'", id);
        Optional<Reservation> optionalReservation = reservationRepository.findReservationsByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.warn("No reservations to change status to canceled found. ID: '{}'", id);
            throw new ReservationNotFoundException("No reservations found");
        }
        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        log.info("The reservations status changed to canceled. ID: '{}'", id);
    }

    @Transactional
    public void delete(UUID id) {
        reservationRepository.deleteById(id);
        log.info("The reservation deleted. ID: '{}'", id);
    }

    @Transactional(readOnly = true)
    public Integer findReservationByBookId(LocalDate localDate, UUID reservationBook) {
        List<Reservation> reservationList = reservationRepository.findReservationByReservationDateAndReservationBook(localDate, reservationBook);
        if (reservationList.isEmpty()) {
            log.warn("No reservations for check available items found");
            return 0;
        }
        return reservationList.size();
    }

    @Transactional(readOnly = true)
    public ReservationsPageDto findReservationByBookId(UUID reservationBook, PageRequest pageRequest) {
        Page<Reservation> reservationList = reservationRepository.findReservationByReservationBook(reservationBook, pageRequest);
        if (reservationList.isEmpty()) {
            log.warn("No reservations by book id found '{}'", reservationBook);
            throw new ReservationsNotFoundException("No reservations found");
        }

        ReservationsPageDto dto = new ReservationsPageDto();
        dto.setReservations(reservationList.get().map(this::toDTOForView).toList());
        dto.setTotalPages(reservationList.getTotalPages());
        dto.setTotalElements(reservationList.getTotalElements());
        log.info("All reservations by book id found '{}'", reservationBook);
        return dto;

    }@Transactional(readOnly = true)
    public Integer findReservationByBookId(UUID reservationBook) {
        List<Reservation> reservationList = reservationRepository.findReservationsByReservationBookAndReservationDateAndReservationStatusNot(
                reservationBook, LocalDate.now(),ReservationStatus.CANCELLED);
        if (reservationList.isEmpty()) {
            log.warn("No reservations by book id found '{}'", reservationBook);
            return 0;
        }
        log.info("All reservations by book id found '{}'", reservationBook);
        return reservationList.stream().map(this::toDTO).toList().size();

    }


    @Transactional(readOnly = true)
    public Integer findReservationUserId(LocalDate localDate, UUID reservationUser) {
        List<Reservation> reservationList = reservationRepository.findReservationByReservationDateAndReservationBook(localDate, reservationUser);
        if (reservationList.isEmpty()) {
            log.warn("No reservations for check available date found");
            return 0;
        }
        return reservationList.size();
    }

    @Transactional(readOnly = true)
    public ReservationsPageDto findReservationByUserId(UUID reservationUser, PageRequest pageRequest) {
        Page<Reservation> reservationList = reservationRepository.findReservationByReservationUser(reservationUser, pageRequest);
        if (reservationList.isEmpty()) {
            log.warn("No reservations by user id found '{}'", reservationUser);
            throw new ReservationsNotFoundException("No reservations found");
        }
        ReservationsPageDto dto = new ReservationsPageDto();
        dto.setReservations(reservationList.get().map(this::toDTOForView).toList());
        dto.setTotalPages(reservationList.getTotalPages());
        dto.setTotalElements(reservationList.getTotalElements());
        log.info("All reservations by user id found '{}'", reservationUser);
        return dto;
    }

    @Transactional(readOnly = true)
    public ReservationsPageDto findAllReservation(PageRequest pageRequest) {
        Page<Reservation> reservationList = reservationRepository.findAllByReservationBookIsNotNullAndReservationUserIsNotNull(pageRequest);
        if (reservationList.isEmpty()) {
            log.warn("No reservations found");
            throw new ReservationsNotFoundException("No reservations found");
        }
        log.info("All reservations found");
        ReservationsPageDto dto = new ReservationsPageDto();
        dto.setReservations(reservationList.stream().map(this::toDTOForView).toList());
        dto.setTotalElements(reservationList.getTotalElements());
        dto.setTotalPages(reservationList.getTotalPages());

        return dto;
    }

    @Transactional(readOnly = true)
    public ReservationDTO findById(UUID id) {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.warn("The reservation by id not found. ID: '{}'", id);
            throw new ReservationNotFoundException("The reservation not found");
        }
        log.info("The reservation by id found. ID: '{}'", id);
        return toDTO(optionalReservation.get());
    }

    @Transactional(readOnly = true)
    public Reservation findByReservationId(UUID id) {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(id);
        if (optionalReservation.isEmpty()) {
            log.warn("The reservation not found. ID: '{}'", id);
            throw new ReservationNotFoundException("The reservation not found");
        }
        log.info("The reservation found. ID: '{}'", id);
        return optionalReservation.get();
    }
    @Transactional(readOnly = true)
    public ReservationsPageDto findAllReservationByParam(String search, Pageable pageable) {
        String decodedSearch = URLDecoder.decode(search, StandardCharsets.UTF_8);
        String searchPattern = "%" + decodedSearch.trim().replaceAll("\\s+", "%") + "%";
        Page<Reservation> reservationList = reservationRepository.findByParam(searchPattern, pageable);
        if (reservationList.isEmpty()) {
            log.warn("No reservations found");
            throw new ReservationsNotFoundException("No reservations found");
        }
        log.info("All reservations found");
        ReservationsPageDto dto = new ReservationsPageDto();
        dto.setReservations(reservationList.stream().map(this::toDTOForView).toList());
        dto.setTotalElements(reservationList.getTotalElements());
        dto.setTotalPages(reservationList.getTotalPages());
        return dto;

    }



    private ReservationDTO toDTO(Reservation reservation) {
        log.info("Mapping reservation entity to dto for response. ID: '{}'", reservation.getReservationId());
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    private Reservation toEntity(ReservationDTO reservationDTO) {
        log.info("Mapping reservationDTO to entity. User: '{}'", reservationDTO.getReservationBook());
        return modelMapper.map(reservationDTO, Reservation.class);
    }

    private ReservationForView toDTOForView(Reservation reservation) {
        log.info("Mapping reservation entity to dto for response. ID: '{}'", reservation.getReservationId());
        return modelMapper.map(reservation, ReservationForView.class);
    }
}
