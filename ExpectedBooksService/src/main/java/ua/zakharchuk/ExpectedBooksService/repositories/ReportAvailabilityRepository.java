package ua.zakharchuk.ExpectedBooksService.repositories;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;

import java.util.List;
import java.util.UUID;
@Repository
public interface ReportAvailabilityRepository extends JpaRepository<ReportAvailability, UUID> {
    List<ReportAvailability> findAllByExpectedBookId(@NotEmpty(message = "The book id field must not be empty.") UUID expectedBookId);
}
