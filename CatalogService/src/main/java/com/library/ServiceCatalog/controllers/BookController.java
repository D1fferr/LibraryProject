package com.library.ServiceCatalog.controllers;

import com.library.ServiceCatalog.dto.*;
import com.library.ServiceCatalog.services.BookService;
import com.library.ServiceCatalog.exceptions.BookNotCreatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/public")
    public ResponseEntity<BookDtoWithTotalElements> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "booksPerPage", defaultValue = "5", required = false) Integer booksPerPage,
                                                       @RequestParam(value = "sortBy", defaultValue = "bookAddedAt") String sortBy,
                                                       @RequestParam(value = "genre", required = false) String genre,
                                                       @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir)
    {
        if (sortBy.equals("bookGenre") && genre !=null && !genre.isEmpty()){
            BookDtoWithTotalElements books = bookService.findAllByGenre(genre, PageRequest.of(page, booksPerPage, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        BookDtoWithTotalElements books = bookService.findAll(PageRequest.of(page, booksPerPage, Sort.by(sortBy)));
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/auth")
    public ResponseEntity<BookDtoWithTotalElements> getAllBooksForAdmin(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(required = false) String search)
    {
        if (search!=null){
            BookDtoWithTotalElements books = bookService.findAllByParam(search ,PageRequest.of(page, 12));
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        BookDtoWithTotalElements books = bookService.findAll(PageRequest.of(page, 12));
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @PostMapping("/auth/for-reservations")
    public ResponseEntity<BookDtoWithTotalElements> getAllBooksForReservations(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                               @RequestParam(value = "booksPerPage", defaultValue = "4", required = false) Integer booksPerPage,
                                                                               @RequestBody BookDtoForReservations uuids
                                                                )
    {
        BookDtoWithTotalElements books = bookService.getBooksById(uuids.getUuids());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/public/author/{bookAuthor}")
    public ResponseEntity<BookDtoWithTotalElements> getAllBooksByAuthor(
            @PathVariable String bookAuthor,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "booksPerPage", defaultValue = "5", required = false) Integer booksPerPage)
    {
        Pageable pageable = PageRequest.of(page, booksPerPage);
        BookDtoWithTotalElements books = bookService.findAllByAuthor(bookAuthor, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/public/genre/{bookGenre}")
    public ResponseEntity<BookDtoWithTotalElements> getAllBooksByGenre(
            @PathVariable String bookGenre,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "booksPerPage", defaultValue = "5", required = false) Integer booksPerPage)
    {
        Pageable pageable = PageRequest.of(page, booksPerPage);
        BookDtoWithTotalElements books = bookService.findAllByGenre(bookGenre, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/public/recently-added-at")
    public ResponseEntity<BookDtoWithTotalElements> getAllRecentlyAddedAtBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "booksPerPage", defaultValue = "5") Integer booksPerPage)
    {
        BookDtoWithTotalElements books = bookService.findAllRecentlyAddedAt(page, booksPerPage);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/public/{book_id}")
    public ResponseEntity<BookDTOForResponseGetBook> getBook(@PathVariable UUID book_id){
        return new ResponseEntity<>(bookService.findById(book_id), HttpStatus.OK);
    }


    @GetMapping("/public/pieces/{book_id}")
    public Integer getBookPieces(@PathVariable UUID book_id){
        return bookService.findById(book_id).getBookItems();
    }

    @PostMapping("/auth/create")
    public ResponseEntity<HttpStatus> createBook(@RequestPart("bookData") @Valid BookDTOForCreate bookDTO,
                                                               @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                               BindingResult bindingResult){
        validateBookFields(bindingResult);
        System.out.println(coverImage.getOriginalFilename());
        BookDTOForResponseCreate bookDTOForResponseCreate = bookService.save(bookDTO, coverImage);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("/auth/delete/{book_id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable UUID book_id){
        bookService.delete(book_id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
    @PatchMapping("/auth/change-book/{id}")
    public ResponseEntity<HttpStatus> changeBook(@PathVariable UUID id,
                                                               @RequestPart("bookData") @Valid BookDTO bookDTO,
                                                               @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                               BindingResult bindingResult){
        validateBookFields(bindingResult);
        BookDTOForResponseCreate bookDTOForResponseCreate = bookService.updateBook(bookDTO, id, coverImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/public/most-popular-books")
    public ResponseEntity<BookDtoWithTotalElements> getMostPopularBooks(@RequestParam(value = "page", defaultValue = "0") Integer page) {

        BookDtoWithTotalElements books = bookService.getMostPopularBooks(page);
        return new ResponseEntity<>(books, HttpStatus.OK);

    }
    @GetMapping("/public/category")
    public ResponseEntity<CategoriesDtoForResponse> getCategories(){
        CategoriesDtoForResponse dto = new CategoriesDtoForResponse();
        dto.setCategories(bookService.findCategories());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    private void validateBookFields(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            log.info("\"Validation failed for book fields: {}", errors);
            throw new BookNotCreatedException(errorMessage.toString());
        }
    }


}
