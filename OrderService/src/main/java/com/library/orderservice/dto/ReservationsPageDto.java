package com.library.orderservice.dto;


import com.library.orderservice.models.Reservation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReservationsPageDto {
    private List<ReservationForView> reservations;
    private int totalPages;
    private long totalElements;
}
