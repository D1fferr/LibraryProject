package ua.zakharchuk.ExpectedBooksService.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ReportAvailabilityNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ReportAvailability;
import ua.zakharchuk.ExpectedBooksService.models.Status;
import ua.zakharchuk.ExpectedBooksService.repositories.ReportAvailabilityRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportAvailabilityService {

    private final ReportAvailabilityRepository reportAvailabilityRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(ReportAvailabilityDTO reportAvailabilityDTO){
        reportAvailabilityRepository.save(toEntity(reportAvailabilityDTO));
    }
    @Transactional
    public void deleteById(UUID id){
        reportAvailabilityRepository.deleteById(id);
    }
    @Transactional
    public void changeStatus(UUID id){
        ReportAvailability reportAvailability = reportAvailabilityRepository.findById(id).orElseThrow(
                ()-> new ReportAvailabilityNotFoundException("Record not found"));
        reportAvailability.setStatus(Status.SENT);
        reportAvailabilityRepository.save(reportAvailability);
    }
    @Transactional(readOnly = true)
    public List<ReportAvailability> findAllByBookId(UUID id){
        return reportAvailabilityRepository.findAllByExpectedBookId(id);
    }

    private ReportAvailabilityDTO toDTO(ReportAvailability reportAvailability){
        return modelMapper.map(reportAvailability, ReportAvailabilityDTO.class);
    }
    private ReportAvailability toEntity(ReportAvailabilityDTO reportAvailabilityDTO){
        return modelMapper.map(reportAvailabilityDTO, ReportAvailability.class);
    }
}
