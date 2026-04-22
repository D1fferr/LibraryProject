package ua.zakharchuk.ExpectedBooksService.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.zakharchuk.ExpectedBooksService.dtos.PageReportAvailabilityErrorDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityErrorDTOBookId;
import ua.zakharchuk.ExpectedBooksService.dtos.ReportAvailabilityErrorDTO;
import ua.zakharchuk.ExpectedBooksService.exceptions.ReportAvailabilityErrorBadRequestException;
import ua.zakharchuk.ExpectedBooksService.services.EmailSenderService;
import ua.zakharchuk.ExpectedBooksService.services.ReportAvailabilityErrorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report-availability-error/auth")
@Slf4j
public class ReportAvailabilityErrorController {

    private final ReportAvailabilityErrorService errorService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/get-all")
    public ResponseEntity<PageReportAvailabilityErrorDTO> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "items-per-page", defaultValue = "5")
            Integer itemsPerPage){
        return new ResponseEntity<>(errorService.findAll(PageRequest.of(page, itemsPerPage)), HttpStatus.OK);
    }
    @GetMapping("/send")
    public ResponseEntity<HttpStatus> sendEmail(
            @RequestBody @Valid ReportAvailabilityErrorDTOBookId reportAvailabilityErrorDTOBookId,
                                                BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String errors = bindingResult.getFieldErrors().toString();
            log.info("Errors found in entity fields. Errors: '{}'", errors);
            throw new ReportAvailabilityErrorBadRequestException(errors);
        }
        emailSenderService.send(reportAvailabilityErrorDTOBookId.getExpectedBookId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}