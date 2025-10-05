package ua.zakharchuk.ExpectedBooksService.dtos;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportAvailabilityDTO {
    @Size(min = 1, message = "The book id field must not be empty.")
    private UUID userId;
    @Size(min = 1, message = "The book id field must not be empty.")
    private UUID expectedBookId;
    @Size(min = 1, message = "The user email field must not be empty.")
    private String userEmail;
}
