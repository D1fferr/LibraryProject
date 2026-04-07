package com.library.EvenService.controllers;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.dto.AnnouncementDTOForGetRequest;
import com.library.EvenService.dto.AnnouncementDTOWithTotalElements;
import com.library.EvenService.services.AnnouncementService;
import com.library.EvenService.utill.NewsNotCreatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
@Slf4j
public class AnnouncementController {

    private final AnnouncementService announcementService;


    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTOForGetRequest> getOneAnnouncement(@PathVariable UUID id){
        return new ResponseEntity<>(announcementService.findOneById(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<AnnouncementDTOWithTotalElements> getAllAnnouncement(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "announcementPerPage", defaultValue = "5") Integer announcementPerPage){

        AnnouncementDTOWithTotalElements dto = announcementService.findAll(PageRequest.of(page, announcementPerPage));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/auth/create")
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestPart("announcementData") @Valid AnnouncementDTO announcementDTO,
                                                              @RequestPart(value = "coverImage", required = false) MultipartFile image,
                                                         BindingResult bindingResult){
        checkAnnouncementErrors(bindingResult);
        announcementService.save(announcementDTO, image);
        return new ResponseEntity<>(announcementDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/auth/change/{id}")
    public ResponseEntity<AnnouncementDTOForGetRequest> changeAnnouncement(
            @PathVariable UUID id,
            @RequestPart("announcementData") @Valid AnnouncementDTO announcementDTO,
            @RequestPart(value = "coverImage", required = false) MultipartFile image,
            BindingResult bindingResult){
        checkAnnouncementErrors(bindingResult);
        AnnouncementDTOForGetRequest dto = announcementService.update(id, announcementDTO, image);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAnnouncement(@PathVariable UUID id){
        announcementService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkAnnouncementErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            log.info("Errors found in entity fields. Errors: '{}'", errors);
            throw new NewsNotCreatedException(errorMessage.toString());
        }
    }

}
