package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.Reservation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReservationsPageDto {
    private List<ReservationDto> reservations;
    private int totalPages;
    private long totalElements;
}