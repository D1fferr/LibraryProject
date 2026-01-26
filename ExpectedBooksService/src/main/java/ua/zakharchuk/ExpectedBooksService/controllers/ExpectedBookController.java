package ua.zakharchuk.ExpectedBooksService.controllers;

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
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOCreate;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBookNotCreatedException;
import ua.zakharchuk.ExpectedBooksService.services.ExpectedBookService;
import ua.zakharchuk.ExpectedBooksService.services.KafkaSenderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expected-book")
@RequiredArgsConstructor
@Slf4j
public class ExpectedBookController {

    private final KafkaSenderService kafkaSenderService;
    private final ExpectedBookService expectedBookService;


    @GetMapping("/{id}")
    public ResponseEntity<ExpectedBookDTO> getOne(@PathVariable UUID id){
        return new ResponseEntity<>(expectedBookService.findById(id), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<ExpectedBookDTO>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "bookPerPage", defaultValue = "5")
            Integer bookPerPage){
        return new ResponseEntity<>(expectedBookService.findAll(PageRequest.of(page, bookPerPage)), HttpStatus.OK);
    }
    @PostMapping("/auth/create")
    public ResponseEntity<ExpectedBookDTO> createExpectedBook(@RequestPart("bookData") @Valid ExpectedBookDTOCreate bookDTO,
                                                              @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                              BindingResult bindingResult){
        checkBookErrors(bindingResult);
        ExpectedBookDTO expectedBookDTO = expectedBookService.save(bookDTO, coverImage);
        return new ResponseEntity<>(expectedBookDTO, HttpStatus.CREATED);
    }
    @PatchMapping("/auth/change/{id}")
    public ResponseEntity<ExpectedBookDTO> changeExpectedBook(@PathVariable UUID id,
                                                              @RequestPart("bookData") @Valid ExpectedBookDTOCreate bookDTO,
                                                              @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                              BindingResult bindingResult){

        checkBookErrors(bindingResult);
        ExpectedBookDTO expectedBookDTO = expectedBookService.update(id, bookDTO, coverImage);
        return new ResponseEntity<>(expectedBookDTO, HttpStatus.OK);
    }
    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable UUID id){
        expectedBookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/auth/add-to-current-books/{id}")
    public ResponseEntity<HttpStatus> addToCurrentBooks(@PathVariable UUID id){
        kafkaSenderService.send(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkBookErrors(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            var errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMessage.append(fieldError.getField()).append(" - ")
                        .append(fieldError.getDefaultMessage()).append(";");
            }
            log.info("Errors found in entity fields. Errors: '{}'", fieldErrors);
            throw new ExpectedBookNotCreatedException(errorMessage.toString());
        }
    }

}
