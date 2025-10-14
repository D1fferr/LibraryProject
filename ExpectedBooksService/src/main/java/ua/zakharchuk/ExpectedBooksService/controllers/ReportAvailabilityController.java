package ua.zakharchuk.ExpectedBooksService.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ReportAvailabilityNotCreatedException;
import ua.zakharchuk.ExpectedBooksService.services.ReportAvailabilityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/report-availability")
@RequiredArgsConstructor
public class ReportAvailabilityController {

    private final ReportAvailabilityService reportAvailabilityService;

    @PostMapping("/add")
    public ResponseEntity<ReportAvailabilityDTO> addReportAvailability(@RequestBody @Valid ReportAvailabilityDTO reportAvailabilityDTO,
                                                                       BindingResult bindingResult) {
        checkReportAvailabilityErrors(bindingResult);
        reportAvailabilityService.save(reportAvailabilityDTO);
        return new ResponseEntity<>(reportAvailabilityDTO, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteReportAvailability(@PathVariable UUID id){
        reportAvailabilityService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkReportAvailabilityErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            throw new ReportAvailabilityNotCreatedException(errorMessage.toString());
        }
    }
}
