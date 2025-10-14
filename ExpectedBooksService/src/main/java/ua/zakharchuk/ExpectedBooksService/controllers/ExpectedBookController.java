package ua.zakharchuk.ExpectedBooksService.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBookNotCreatedException;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;
import ua.zakharchuk.ExpectedBooksService.services.ExpectedBookService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("expected-book")
@RequiredArgsConstructor
public class ExpectedBookController {
    private final KafkaTemplate<String, ExpectedBookDTOForKafka> expectedBookKafkaTemplate;
    private final ExpectedBookService expectedBookService;


    @GetMapping("/{id}")
    public ResponseEntity<ExpectedBookDTO> getOne(@PathVariable UUID id){
        return new ResponseEntity<>(expectedBookService.findOneBook(id), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<ExpectedBookDTO>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "bookPerPage", defaultValue = "5")
            Integer bookPerPage){
        return new ResponseEntity<>(expectedBookService.findAll(PageRequest.of(page, bookPerPage)), HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<ExpectedBookDTO> createExpectedBook(@RequestBody @Valid ExpectedBookDTO expectedBookDTO,
                                                              BindingResult bindingResult){
        checkBookErrors(bindingResult);
        expectedBookService.save(expectedBookDTO);
        return new ResponseEntity<>(expectedBookDTO, HttpStatus.CREATED);
    }
    @PatchMapping("/change/{id}")
    public ResponseEntity<ExpectedBookDTO> changeExpectedBook(
            @PathVariable UUID id,
            @RequestBody @Valid ExpectedBookDTO expectedBookDTO,
            BindingResult bindingResult){

        checkBookErrors(bindingResult);
        expectedBookService.update(id, expectedBookDTO);
        return new ResponseEntity<>(expectedBookDTO, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable UUID id){
        expectedBookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/add-to-current-books/{id}")
    public ResponseEntity<HttpStatus> addToCurrentBooks(@PathVariable UUID id){

        ExpectedBookDTOForKafka expectedBookDTOForKafka = expectedBookService.findOneBookForKafka(id);
        expectedBookKafkaTemplate.send("expected-book-topic", expectedBookDTOForKafka);
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
            throw new ExpectedBookNotCreatedException(errorMessage.toString());
        }
    }

}
