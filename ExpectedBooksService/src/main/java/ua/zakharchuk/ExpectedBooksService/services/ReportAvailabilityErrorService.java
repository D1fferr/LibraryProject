package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.exceptions.ReportAvailabilityNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityErrorRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportAvailabilityErrorService {

    private final ReportAvailabilityErrorRepository errorRepository;

    @Transactional
    public void save(ReportAvailabilityError error){
        errorRepository.save(error);
    }
    @Transactional(readOnly = true)
    public List<ReportAvailabilityError> findAllByStatus(Status status){
        return errorRepository.findAllByStatus(status);
    }
    @Transactional
    public void deleteById(UUID id){
        errorRepository.deleteById(id);
    }
    @Transactional
    public void updateStatus(UUID id){
        ReportAvailabilityError error =  errorRepository.findById(id)
                .orElseThrow(()->new ReportAvailabilityNotFoundException("Record not found"));
        error.setStatus(Status.SENT);

    }
    @Transactional
    public void updateStatusWithError(UUID id, String error){
        ReportAvailabilityError reportAvailabilityError =  errorRepository.findById(id)
                .orElseThrow(()->new ReportAvailabilityNotFoundException("Record not found"));
        reportAvailabilityError.setStatus(Status.CREATED);
        reportAvailabilityError.setError(error);

    }


}
