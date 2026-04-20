package com.library.orderservice.controllers;

import com.library.orderservice.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.library.orderservice.models.Reservation;
import com.library.orderservice.services.CrossServerRequestService;
import com.library.orderservice.services.KafkaSenderService;
import com.library.orderservice.services.ReservationService;
import com.library.orderservice.util.ReservationNotCreatedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final CrossServerRequestService crossServerRequestService;
    private final KafkaSenderService kafkaSenderService;
    @GetMapping("/auth/view_for_the_user/{id}")
    public ResponseEntity<ReservationsPageDto> viewReservationForOneUser(@PathVariable UUID id,
                                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(value = "reservationPerPage", defaultValue = "5") Integer reservationPerPage){
        ReservationsPageDto reservationDTOS = reservationService
                .findReservationByUserId(id, PageRequest.of(page, reservationPerPage));
        return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
    }
    @GetMapping("/auth/view_for_the_book/{id}")
    public ResponseEntity<ReservationsPageDto> viewReservationForOneBook(@PathVariable UUID id,
                                                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                          @RequestParam(value = "reservationPerPage", defaultValue = "5") Integer reservationPerPage){
        ReservationsPageDto reservationDTOS = reservationService
                .findReservationByBookId(id, PageRequest.of(page, reservationPerPage));
        return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
    }
    @GetMapping("/auth/view_all")
    public ResponseEntity<ReservationsPageDto> viewAllReservation(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                              @RequestParam(value = "reservationPerPage", defaultValue = "5") Integer reservationPerPage,
                                                                              @RequestParam(value = "sortBy", defaultValue = "reservationDate") String sortBy,
                                                                              @RequestParam(value = "search", required = false) String search){

        if (search!=null){
            ReservationsPageDto reservationDTOS = reservationService
                    .findAllReservationByParam(search, PageRequest.of(page, reservationPerPage, Sort.by(sortBy)));
            return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
        }
        ReservationsPageDto reservationDTOS = reservationService
                .findAllReservation(PageRequest.of(page, reservationPerPage, Sort.by(sortBy)));
        return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
    }
    @GetMapping("/auth/view/{id}")
    public ResponseEntity<ReservationDTO> viewOne(@PathVariable UUID id){
        ReservationDTO reservationDTO = reservationService.findById(id);
        return new ResponseEntity<>(reservationDTO, HttpStatus.OK);
    }

    @PostMapping("/auth/create")
    public ResponseEntity<HttpStatus> createReservation(@RequestBody @Valid ReservationDTO reservationDTO,
                                                        BindingResult bindingResult){

        checkErrorsReservation(bindingResult);
        crossServerRequestService.checkAvailableDate(reservationDTO);

        Reservation reservation = reservationService.save(reservationDTO);
        kafkaSenderService.send(reservation.getReservationBook().toString());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @PatchMapping("/auth/change_date/{id}")
    public ResponseEntity<HttpStatus> changeReservationDate(@RequestBody @Valid ReservationDTOForChangeDate reservationDTO,
                                                        BindingResult bindingResult, @PathVariable UUID id){
        checkErrorsReservation(bindingResult);
        ReservationDTO reservationDTOParams = reservationService.findById(id);
        reservationDTOParams.setReservationDate(reservationDTO.getReservationDate());
        crossServerRequestService.checkAvailableDate(reservationDTOParams);
        reservationService.updateDate(id, reservationDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/auth/change_status/{id}")
    public ResponseEntity<HttpStatus> changeReservationStatus(@PathVariable UUID id){
        reservationService.updateStatusToConfirmed(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable UUID id){
        reservationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkErrorsReservation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            log.info("Errors in entity fields. Errors: '{}'", errorMessage);
            throw new ReservationNotCreatedException(errorMessage.toString());
        }
    }

}