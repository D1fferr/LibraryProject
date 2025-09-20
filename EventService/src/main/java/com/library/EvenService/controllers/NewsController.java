package com.library.EvenService.controllers;

import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.services.NewsService;
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
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<NewsDTO>> getAllNews(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "newsPerPage", defaultValue = "5") Integer newsPerPage){
        return new ResponseEntity<>(newsService.findAll(PageRequest.of(page, newsPerPage)), HttpStatus.OK);
    }
    @GetMapping("/get-one/{id}")
    public ResponseEntity<NewsDTO> getOneNews(@PathVariable int id){
        return new ResponseEntity<>(newsService.findOneById(id), HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<NewsDTO> createNews(@RequestBody @Valid NewsDTO newsDTO,
                                                 BindingResult bindingResult){

        checkNewsErrors(bindingResult);
        newsService.save(newsDTO);
        return new ResponseEntity<>(newsDTO, HttpStatus.CREATED);

    }
    @PatchMapping("/change/{id}")
    public ResponseEntity<NewsDTO> changeNews(@PathVariable int id,
                                              @RequestBody @Valid NewsDTO newsDTO,
                                              BindingResult bindingResult){
        checkNewsErrors(bindingResult);
        newsService.update(id, newsDTO);
        return new ResponseEntity<>(newsDTO, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable int id){
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
            throw new NewsNotCreatedException(errorMessage.toString());
        }
    }

}
