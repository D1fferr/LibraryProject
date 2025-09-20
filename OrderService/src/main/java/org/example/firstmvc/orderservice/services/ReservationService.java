package org.example.firstmvc.orderservice.services;

import org.example.firstmvc.orderservice.dto.ReservationDTO;
import org.example.firstmvc.orderservice.models.Reservation;
import org.example.firstmvc.orderservice.models.ReservationStatus;
import org.example.firstmvc.orderservice.repositories.ReservationRepository;
import org.example.firstmvc.orderservice.util.BookNotFoundException;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.modelmapper.ModelMapper;
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
public class ReservationService {
    private final RestTemplate restTemplate;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    public ReservationService(RestTemplate restTemplate, ReservationRepository reservationRepository, ModelMapper modelMapper) {
        this.restTemplate = restTemplate;
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public void save(ReservationDTO reservationDTO){

        Reservation reservation = toEntity(reservationDTO);
        reservation.setReservationStatus(ReservationStatus.CREATED);
        reservation.setCreatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
    @Transactional
    public void update(UUID id, ReservationDTO reservationDTO){
        Reservation reservation = reservationRepository.findReservationsByReservationId(id)
                .orElseThrow(()->new ReservationNotFoundException("No reservations found"));
        if (reservationDTO.getReservationDate()!=null)
            reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setReservationStatus(ReservationStatus.CREATED);
        reservationRepository.save(reservation);
    }
    @Transactional
    public void updateStatus(UUID id, ReservationDTO reservationDTO){
        Reservation reservation = reservationRepository.findReservationsByReservationId(id)
                .orElseThrow(()->new ReservationNotFoundException("No reservations found"));
        if (reservationDTO.getReservationStatus().equals(ReservationStatus.CONFIRMED))
            reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }
    @Transactional
    public void delete(int id){
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByBookId(LocalDate localDate, UUID reservationBook) {
        return reservationRepository.findReservationByReservationDateAndReservationBook(localDate, reservationBook)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByBookId(UUID reservationBook, PageRequest pageRequest) {
        return reservationRepository.findReservationByReservationBook(reservationBook, pageRequest)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationUserId(LocalDate localDate, UUID reservationUser){
        return reservationRepository.findReservationByReservationDateAndReservationBook(localDate,reservationUser)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByUserId(UUID reservationUser, PageRequest pageRequest){
        return reservationRepository.findReservationByReservationUser(reservationUser, pageRequest)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<ReservationDTO> findAllReservation(PageRequest pageRequest){
        return reservationRepository.findAll(pageRequest)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public Optional<ReservationDTO> findById(UUID id){
        return reservationRepository.findByReservationId(id).map(this::toDTO);
    }

    public Integer booksAvailable(UUID bookId){
        try {
            return restTemplate.getForObject(
                    "http://localhost:8081/book/pieces/{book_id}",
                    Integer.class,
                    bookId
            );
        } catch (Exception e) {
            throw new BookNotFoundException("Book not found");
        }
    }

    private ReservationDTO toDTO(Reservation reservation){
        return modelMapper.map(reservation, ReservationDTO.class);
    }
    private Reservation toEntity(ReservationDTO reservationDTO){
        return modelMapper.map(reservationDTO, Reservation.class);
    }
}
