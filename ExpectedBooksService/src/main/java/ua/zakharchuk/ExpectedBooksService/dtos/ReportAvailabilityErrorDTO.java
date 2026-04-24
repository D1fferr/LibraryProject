package ua.zakharchuk.ExpectedBooksService.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportAvailabilityErrorDTO {
    private UUID id;
    private UUID userId;
    private UUID expectedBookId;
    private String userEmail;
    private Status status;
    private String username;
    private String error;

}
