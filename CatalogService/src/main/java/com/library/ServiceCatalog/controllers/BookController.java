package com.library.ServiceCatalog.controllers;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.dto.BookDTOForResponseCreate;
import com.library.ServiceCatalog.dto.BookDTOForResponseGetBook;
import com.library.ServiceCatalog.services.BookService;
import com.library.ServiceCatalog.util.BookNotCreatedException;
import com.library.ServiceCatalog.util.BooksNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<List<BookDTOForResponseGetBook>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "booksPerPage", defaultValue = "5") Integer booksPerPage,
                                                       @RequestParam(value = "sortBy", defaultValue = "bookName") String sortBy)
    {
        List<BookDTOForResponseGetBook> books = bookService.findAll(PageRequest.of(page, booksPerPage, Sort.by(sortBy)));
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/recently-added-at")
    public ResponseEntity<List<BookDTOForResponseGetBook>> getAllRecentlyAddedAtBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "booksPerPage", defaultValue = "5") Integer booksPerPage)
    {
        List<BookDTOForResponseGetBook> books = bookService.findAllRecentlyAddedAt(page, booksPerPage);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/{book_id}")
    public ResponseEntity<BookDTOForResponseGetBook> getBook(@PathVariable UUID book_id){
        return new ResponseEntity<>(bookService.findById(book_id), HttpStatus.OK);
    }
    @GetMapping("/pieces/{book_id}")
    public Integer getBookPieces(@PathVariable UUID book_id){
        return bookService.findById(book_id).getBookPieces();
    }

    @PostMapping("/auth/create")
    public ResponseEntity<BookDTOForResponseCreate> createBook(@RequestPart("bookData") @Valid BookDTO bookDTO,
                                                               @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                               BindingResult bindingResult){
        errorCheckingWhenChangingBookFields(bindingResult);
        BookDTOForResponseCreate bookDTOForResponseCreate = bookService.save(bookDTO, coverImage);
        return new ResponseEntity<>(bookDTOForResponseCreate, HttpStatus.CREATED);
    }
    @DeleteMapping("/auth/delete/{book_id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable UUID book_id){
        bookService.delete(book_id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
    @PatchMapping("/auth/change-book/{id}")
    public ResponseEntity<BookDTOForResponseCreate> changeBook(@PathVariable UUID id,
                                                               @RequestPart("bookData") @Valid BookDTO bookDTO,
                                                               @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                               BindingResult bindingResult){
        errorCheckingWhenChangingBookFields(bindingResult);
        BookDTOForResponseCreate bookDTOForResponseCreate = bookService.updateBook(bookDTO, id, coverImage);
        return new ResponseEntity<>(bookDTOForResponseCreate, HttpStatus.OK);
    }
    @GetMapping("/most-popular-books")
    public ResponseEntity<List<BookDTO>> getMostPopularBooks(@RequestParam(value = "page", defaultValue = "0") Integer page) {

        List<BookDTO> books = bookService.getMostPopularBooks(page);
        return new ResponseEntity<>(books, HttpStatus.OK);

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
