package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityErrorDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBooksNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityErrorRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAvailabilityErrorService {

    private final ReportAvailabilityErrorRepository errorRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(ReportAvailabilityError error){
        errorRepository.save(error);
    }

    @Transactional(readOnly = true)
    public List<ReportAvailabilityErrorDTO> findAll(Pageable pageable) {
        List<ReportAvailabilityErrorDTO> reportAvailabilityErrorDTOS = errorRepository
                .findAllByStatus(Status.CREATED, pageable).stream().map(this::toDTO).toList();
        if (reportAvailabilityErrorDTOS.isEmpty())
            throw new ExpectedBooksNotFoundException("The selected record not found.");
        return reportAvailabilityErrorDTOS;
    }

    private ReportAvailabilityErrorDTO toDTO(ReportAvailabilityError reportAvailabilityError){
        return modelMapper.map(reportAvailabilityError, ReportAvailabilityErrorDTO.class);
    }

}
