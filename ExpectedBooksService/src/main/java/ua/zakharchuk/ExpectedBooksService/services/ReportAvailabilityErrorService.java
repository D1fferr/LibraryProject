package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityErrorRepository;

@Service
@RequiredArgsConstructor
public class ReportAvailabilityErrorService {

    private final ReportAvailabilityErrorRepository errorRepository;

    @Transactional
    public void save(ReportAvailabilityError error){
        errorRepository.save(error);
    }

}
