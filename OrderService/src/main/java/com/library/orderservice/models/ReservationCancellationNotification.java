package com.library.orderservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reservation_cancellation_notifications")
public class ReservationCancellationNotification {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id")
    private UUID id;
    @NotNull(message = "Field Reservations id must not be empty")
    @Column(name = "reservation_id")
    private UUID reservationId;
    @NotNull(message = "Message must not be empty")
    @Column(name = "message")
    private String message;
}
