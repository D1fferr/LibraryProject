package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.dtos.PageReportAvailabilityErrorDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityErrorDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBooksNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityErrorRepository;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAvailabilityErrorService {

    private final ReportAvailabilityErrorRepository errorRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(ReportAvailabilityError error){
        errorRepository.save(error);
        log.info("Report availability errors saved. ID: '{}'", error.getId());
    }

    @Transactional(readOnly = true)
    public PageReportAvailabilityErrorDTO findAll(Pageable pageable) {
        Page<ReportAvailabilityError> reportAvailabilityErrorDTOS = errorRepository
                .findAllByStatus(Status.CREATED, pageable);
        if (reportAvailabilityErrorDTOS.isEmpty()){
            log.warn("Failed to find all report availability errors. The selected record not found");
            throw new ExpectedBooksNotFoundException("The selected record not found.");
        }
        PageReportAvailabilityErrorDTO dto = new PageReportAvailabilityErrorDTO();
        dto.setDtoList(reportAvailabilityErrorDTOS.stream().map(this::toDTO).toList());
        dto.setTotalPages(reportAvailabilityErrorDTOS.getTotalPages());
        dto.setTotalElements(reportAvailabilityErrorDTOS.getNumberOfElements());
        return dto;
    }

    private ReportAvailabilityErrorDTO toDTO(ReportAvailabilityError reportAvailabilityError){
        log.info("Mapping report availability entity to dto for response. ID: '{}'", reportAvailabilityError.getId());
        return modelMapper.map(reportAvailabilityError, ReportAvailabilityErrorDTO.class);
    }

}
