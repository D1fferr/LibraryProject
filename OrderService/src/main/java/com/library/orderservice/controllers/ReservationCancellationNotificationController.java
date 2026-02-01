package com.library.orderservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.library.orderservice.dto.JoinDTOForCancelledReservations;
import com.library.orderservice.dto.ReservationCancellationNotificationDTO;
import com.library.orderservice.services.EmailSenderService;
import com.library.orderservice.services.ReservationCancellationNotificationService;
import com.library.orderservice.util.ReservationNotCreatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cancel-reservation")
@Slf4j
public class ReservationCancellationNotificationController {
    private final ReservationCancellationNotificationService service;
    private final EmailSenderService emailSenderService;

    @PostMapping("/cancel/{id}")
    public ResponseEntity<ReservationCancellationNotificationDTO> cancelReservation(
            @PathVariable UUID id,
            @RequestBody @Valid ReservationCancellationNotificationDTO dto,
                                                                                    BindingResult bindingResult) {
        checkErrorsReservation(bindingResult);
        service.save(dto, id);
        emailSenderService.send(dto, id);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<JoinDTOForCancelledReservations>> getAllCanceledReservationsForUser(@PathVariable UUID id){

        List<JoinDTOForCancelledReservations> reservations = service.findAllReservationsForUser(id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
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
