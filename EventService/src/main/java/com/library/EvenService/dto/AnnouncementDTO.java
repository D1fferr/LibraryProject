package com.library.EvenService.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementDTO {
    @Size(min = 1, max = 1000, message = "The announcement must be between 1 and 1000 characters long.")
    private String body;
    @Size(min = 1, max = 100, message = "Announcement name must be between 1 and 100 characters long. ")
    private String name;
    @Size(min = 1, message = "Announcement type must not be empty.")
    private String type;
    @FutureOrPresent(message = "The announcement date cannot be in the past.")
    private LocalDate date;
    private String photo;
    private Boolean addEvent;

}
