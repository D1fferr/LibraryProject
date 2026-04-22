package ua.zakharchuk.ExpectedBooksService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.services.ReportAvailabilityErrorService;

import java.util.List;
import java.util.UUID;

public interface ReportAvailabilityErrorRepository extends JpaRepository<ReportAvailabilityError, UUID> {

    Page<ReportAvailabilityError> findAllByStatus(Status status, Pageable pageable);
}
