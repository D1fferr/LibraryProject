package ua.zakharchuk.ExpectedBooksService.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.services.ReportAvailabilityErrorService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportAvailabilityErrorRepository extends JpaRepository<ReportAvailabilityError, UUID> {

    Page<ReportAvailabilityError> findAllByStatus(Status status, Pageable pageable);

    Optional<ReportAvailabilityError> findByExpectedBookId(@NotNull(message = "The book id field must not be empty.") UUID expectedBookId);
}
