package org.example.firstmvc.orderservice.controllers;

import jakarta.validation.Valid;
import org.example.firstmvc.orderservice.dto.ReservationDTO;
import org.example.firstmvc.orderservice.services.ReservationService;
import org.example.firstmvc.orderservice.util.ReservationNotAllowedException;
import org.example.firstmvc.orderservice.util.ReservationNotCreatedException;
import org.example.firstmvc.orderservice.util.ReservationNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping("/view_for_the_user/{id}")
    public ResponseEntity<List<ReservationDTO>> viewReservationForOneUser(@PathVariable UUID id,
                                                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                          @RequestParam(value = "reservationPerPage", defaultValue = "5") Integer reservationPerPage){
        List<ReservationDTO> reservationDTOS = reservationService
                .findReservationByUserId(id, PageRequest.of(page, reservationPerPage));
        if (reservationDTOS.isEmpty())
            throw new ReservationNotFoundException("No reservations found");
        return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
    }
    @GetMapping("/view_for_the_book/{id}")
    public ResponseEntity<List<ReservationDTO>> viewReservationForOneBook(@PathVariable UUID id,
                                                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                          @RequestParam(value = "reservationPerPage", defaultValue = "5") Integer reservationPerPage){
        List<ReservationDTO> reservationDTOS = reservationService
                .findReservationByBookId(id, PageRequest.of(page, reservationPerPage));
        if (reservationDTOS.isEmpty())
            throw new ReservationNotFoundException("No reservations found");
        return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
    }
    @GetMapping("/view_all")
    public ResponseEntity<List<ReservationDTO>> viewAllReservation(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(value = "reservationPerPage", defaultValue = "5") Integer reservationPerPage,
                                                                   @RequestParam(value = "sortBy", defaultValue = "reservationDate") String sortBy){
        List<ReservationDTO> reservationDTOS = reservationService
                .findAllReservation(PageRequest.of(page, reservationPerPage, Sort.by(sortBy)));
        if (reservationDTOS.isEmpty())
            throw new ReservationNotFoundException("No reservations found");
        return new ResponseEntity<>(reservationDTOS, HttpStatus.OK);
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<ReservationDTO> viewOne(@PathVariable UUID id){
        Optional<ReservationDTO> reservationDTO = reservationService.findById(id);
        if (reservationDTO.isEmpty())
            throw new ReservationNotFoundException("No reservations found");
        return new ResponseEntity<>(reservationDTO.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createReservation(@RequestBody @Valid ReservationDTO reservationDTO,
                                                        BindingResult bindingResult){

        checkErrorsReservation(bindingResult);
        checkAvailableDate(reservationDTO);

        reservationService.save(reservationDTO);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @PatchMapping("/change_date/{id}")
    public ResponseEntity<HttpStatus> changeReservationDate(@RequestBody @Valid ReservationDTO reservationDTO,
                                                        BindingResult bindingResult, @PathVariable UUID id){
        checkErrorsReservation(bindingResult);
        ReservationDTO reservationDTOParams = reservationService.findById(id)
                .orElseThrow(()->new ReservationNotFoundException("No reservations found"));
        reservationDTOParams.setReservationDate(reservationDTO.getReservationDate());
        checkAvailableDate(reservationDTOParams);
        reservationService.update(id, reservationDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/change_status/{id}")
    public ResponseEntity<HttpStatus> changeReservationStatus(@RequestBody ReservationDTO reservationDTO,
                                                       @PathVariable UUID id){
        reservationService.updateStatus(id, reservationDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable Integer id){
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
            throw new ReservationNotCreatedException(errorMessage.toString());
        }
    }
    private void checkAvailableDate(ReservationDTO reservationDTO){
        int booksAvailable = reservationService.booksAvailable(reservationDTO.getReservationBook());
        if (reservationService.findReservationByBookId(reservationDTO.getReservationDate(), reservationDTO.getReservationBook()).size()>booksAvailable)
            throw new ReservationNotAllowedException("Reservations for the selected date are not available.");
        if (reservationService.findReservationUserId(reservationDTO.getReservationDate(), reservationDTO.getReservationUser()).size()>5)
            throw new ReservationNotAllowedException("You cannot reserve more than 5 books in 1 day. ");
    }
}