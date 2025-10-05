package ua.zakharchuk.ExpectedBooksService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_availability")
public class ReportAvailability {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "report_availability_id")
    private UUID id;
    @Column(name = "user_id")
    @NotEmpty(message = "The user id field must not be empty.")
    private UUID userId;
    @Column(name = "expected_book_id")
    @NotEmpty(message = "The book id field must not be empty.")
    private UUID expectedBookId;
    @Column(name = "user_email")
    @NotEmpty(message = "The user email field must not be empty.")
    private String userEmail;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}
