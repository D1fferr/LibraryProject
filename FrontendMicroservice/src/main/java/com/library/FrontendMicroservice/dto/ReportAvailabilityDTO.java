package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportAvailabilityDTO {
    @NotNull(message = "The book id field must not be empty.")
    private UUID userId;
    @NotNull(message = "The book id field must not be empty.")
    private UUID expectedBookId;
    @Size(min = 1, message = "The user email field must not be empty.")
    private String userEmail;
    @Size(min = 1, message = "The username field must not be empty.")
    private String username;
}
