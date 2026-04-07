package com.library.EvenService.controllers;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.dto.NewsDTOForGetRequest;
import com.library.EvenService.dto.NewsDtoWithTotalElements;
import com.library.EvenService.services.NewsService;
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
@RequestMapping("/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/get-all")
    public ResponseEntity<NewsDtoWithTotalElements> getAllNews(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(value = "newsPerPage", defaultValue = "5") Integer newsPerPage){

        NewsDtoWithTotalElements dto = newsService.findAll(PageRequest.of(page, newsPerPage));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/get-one/{id}")
    public ResponseEntity<NewsDTOForGetRequest> getOneNews(@PathVariable UUID id){
        return new ResponseEntity<>(newsService.findOneById(id), HttpStatus.OK);
    }
    @PostMapping("/auth/create")
    public ResponseEntity<NewsDTO> createNews(@RequestPart("newsData") @Valid NewsDTO newsDTO,
                                              @RequestPart(value = "coverImage", required = false) MultipartFile image,
                                                 BindingResult bindingResult){

        checkNewsErrors(bindingResult);
        newsService.save(newsDTO, image);
        return new ResponseEntity<>(newsDTO, HttpStatus.CREATED);

    }
    @PatchMapping("/auth/change/{id}")
    public ResponseEntity<NewsDTOForGetRequest> changeNews(@PathVariable UUID id,
                                              @RequestPart("newsData") @Valid NewsDTO newsDTO,
                                              @RequestPart(value = "coverImage", required = false) MultipartFile image,
                                              BindingResult bindingResult){
        checkNewsErrors(bindingResult);
        NewsDTOForGetRequest dto = newsService.update(id, newsDTO, image);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable UUID id){
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkNewsErrors(BindingResult bindingResult) {
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
