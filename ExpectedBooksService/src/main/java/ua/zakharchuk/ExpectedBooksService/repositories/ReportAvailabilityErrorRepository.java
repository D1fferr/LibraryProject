package ua.zakharchuk.ExpectedBooksService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;

import java.util.List;
import java.util.UUID;

public interface ReportAvailabilityErrorRepository extends JpaRepository<ReportAvailabilityError, UUID> {
    List<ReportAvailabilityError> findAllByStatus(Status status);

    List<ReportAvailabilityError> findByStatus(Status status);
}
