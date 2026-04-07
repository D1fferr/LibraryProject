package com.library.EvenService.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AnnouncementDTOForGetRequest {
    private UUID id;
    @Size(min = 1, max = 1000, message = "The announcement must be between 1 and 1000 characters long.")
    private String body;
    @Size(min = 1, max = 100, message = "Announcement name must be between 1 and 100 characters long. ")
    private String name;
    @Size(min = 1, message = "Announcement type must not be empty.")
    private String type;
    @FutureOrPresent(message = "The announcement date cannot be in the past.")
    private LocalDate date;
    private String photo;
}
