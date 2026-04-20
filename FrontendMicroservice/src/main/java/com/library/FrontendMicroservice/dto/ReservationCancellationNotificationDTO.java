package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReservationCancellationNotificationDTO {

    @NotNull(message = "Field Reservations id must not be empty")
    private UUID reservationId;
    @NotNull(message = "Message must not be empty")
    private String message;

}