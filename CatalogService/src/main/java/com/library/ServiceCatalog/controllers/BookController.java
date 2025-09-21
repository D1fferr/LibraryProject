package com.library.ServiceCatalog.controllers;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.models.Book;
import com.library.ServiceCatalog.services.BookService;
import com.library.ServiceCatalog.util.BookAlreadyExitException;
import com.library.ServiceCatalog.util.BookNotCreatedException;
import com.library.ServiceCatalog.util.BooksNotFoundException;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final MessageSource messageSource;

    public BookController(BookService bookService, MessageSource messageSource) {
        this.bookService = bookService;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public List<BookDTO> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "booksPerPage", defaultValue = "5") Integer booksPerPage,
                                     @RequestParam(value = "sortBy", defaultValue = "bookName") String sortBy)
    {
        List<BookDTO> bookDTOs = bookService.findAll(PageRequest.of(page, booksPerPage, Sort.by(sortBy)));
        if (!bookDTOs.isEmpty())
            return bookDTOs;
        else {
            throw new BooksNotFoundException(messageSource
                    .getMessage("books.not.found.message", new Object[0], Locale.ENGLISH));
        }
    }
    @GetMapping("/recentlyAddedAt")
    public List<BookDTO> getAllRecentlyAddedAtBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "booksPerPage", defaultValue = "5") Integer booksPerPage)
    {
        List<BookDTO> bookDTOs = bookService.findAllRecentlyAddedAt(page, booksPerPage);
        if (!bookDTOs.isEmpty())
            return bookDTOs;
        else {
            throw new BooksNotFoundException(messageSource
                    .getMessage("books.not.found.message", new Object[0], Locale.ENGLISH));
        }
    }
    @GetMapping("/{book_id}")
    public BookDTO getBook(@PathVariable UUID book_id){
        return bookService.findById(book_id);
    }
    @GetMapping("/pieces/{book_id}")
    public Integer getBookPieces(@PathVariable UUID book_id){
        return bookService.findById(book_id).getBookPieces();
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createBook(@RequestBody @Valid BookDTO bookDTO,
                                              BindingResult bindingResult){
        errorCheckingWhenChangingBookFields(bindingResult);
        Optional<Book> existingBook = bookService
                .findByNameAndAuthor(bookDTO.getBookName(), bookDTO.getBookAuthor());
        if (existingBook.isPresent())
                throw new BookAlreadyExitException(messageSource
                        .getMessage("book.already.exist.message", new Object[0], Locale.ENGLISH));

        bookService.save(bookDTO);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{book_id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable UUID book_id){
        bookService.delete(book_id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
    @PatchMapping("/changeBook/{id}")
    public ResponseEntity<HttpStatus> changeBook(@PathVariable UUID id,
                                                 @RequestBody @Valid BookDTO bookDTO,
                                                 BindingResult bindingResult){
        errorCheckingWhenChangingBookFields(bindingResult);
        bookService.updateBook(bookDTO, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void errorCheckingWhenChangingBookFields(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new BookNotCreatedException(errorMessage.toString());
        }
    }


}
