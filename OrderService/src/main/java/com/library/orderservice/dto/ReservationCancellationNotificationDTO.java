package com.library.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReservationCancellationNotificationDTO {

    @NotNull(message = "Field Reservations id must not be empty")
    private UUID reservationId;
    @NotNull(message = "Message must not be empty")
    private String message;

}
