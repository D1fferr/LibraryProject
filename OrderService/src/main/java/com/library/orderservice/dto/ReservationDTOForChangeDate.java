package com.library.orderservice.dto;

import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTOForChangeDate {
    @Future(message = "You cannot select the booking date to be in the past or today.")
    private LocalDate reservationDate;
}
