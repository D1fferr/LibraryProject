package ua.zakharchuk.ExpectedBooksService.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ReportAvailabilityNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailabilityError;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAvailabilityService {

    private final ReportAvailabilityRepository reportAvailabilityRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(ReportAvailabilityDTO reportAvailabilityDTO){
        ReportAvailability reportAvailability = toEntity(reportAvailabilityDTO);
        reportAvailability.setStatus(Status.CREATED);
        reportAvailabilityRepository.save(reportAvailability);
        log.info("Saved report availability. ID: '{}'", reportAvailability.getId());
    }
    @Transactional
    public void deleteById(UUID id){
        reportAvailabilityRepository.deleteById(id);
        log.info("Deleted report availability. ID: '{}'", id);
    }
    @Transactional
    public void changeStatus(UUID id){
        Optional<ReportAvailability> optionalReportAvailability = reportAvailabilityRepository.findById(id);
        if (optionalReportAvailability.isEmpty()){
            log.warn("Failed to find report availability. Record not found. ID: '{}'", id);
            throw new ReportAvailabilityNotFoundException("Record not found");
        }
        ReportAvailability reportAvailability = optionalReportAvailability.get();
        reportAvailability.setStatus(Status.SENT);
        reportAvailabilityRepository.save(reportAvailability);
        log.info("Change status report availability to sent. ID: '{}'", reportAvailability.getId());
    }
    @Transactional(readOnly = true)
    public List<ReportAvailability> findAllByBookId(UUID id){
        log.info("Finding all report availability by book id");
        return reportAvailabilityRepository.findAllByExpectedBookId(id);
    }

    private ReportAvailability toEntity(ReportAvailabilityDTO reportAvailabilityDTO){
        log.info("Mapping report availabilityDTO to entity. User id: '{}'", reportAvailabilityDTO.getUserId());
        return modelMapper.map(reportAvailabilityDTO, ReportAvailability.class);
    }
}
