package ua.zakharchuk.ExpectedBooksService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;

import java.util.UUID;
@Repository
public interface ReportAvailabilityRepository extends JpaRepository<ReportAvailability, UUID> {
}
