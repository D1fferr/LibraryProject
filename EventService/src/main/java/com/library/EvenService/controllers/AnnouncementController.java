package com.library.EvenService.controllers;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.services.AnnouncementService;
import com.library.EvenService.utill.NewsNotCreatedException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getOneAnnouncement(@PathVariable int id){
        return new ResponseEntity<>(announcementService.findOneById(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncement(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "announcementPerPage", defaultValue = "5") Integer announcementPerPage){
        return new ResponseEntity<>(announcementService.findAll(
                PageRequest.of(page, announcementPerPage)), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestBody @Valid AnnouncementDTO announcementDTO,
                                                         BindingResult bindingResult){
        checkAnnouncementErrors(bindingResult);
        announcementService.save(announcementDTO);
        return new ResponseEntity<>(announcementDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/change/{id}")
    public ResponseEntity<AnnouncementDTO> changeAnnouncement(
            @PathVariable int id,
            @RequestBody @Valid AnnouncementDTO announcementDTO,
            BindingResult bindingResult){
        checkAnnouncementErrors(bindingResult);
        announcementService.update(id, announcementDTO);
        return new ResponseEntity<>(announcementDTO, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAnnouncement(@PathVariable int id){
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
            throw new NewsNotCreatedException(errorMessage.toString());
        }
    }

}
