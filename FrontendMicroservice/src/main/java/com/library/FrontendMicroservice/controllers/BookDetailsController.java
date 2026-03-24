package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.BookDto;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
//import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.models.Reservation;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.ExpectedBookService;
import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.testclasses.BookServiceTest;
import com.library.FrontendMicroservice.testclasses.ExpectedBookServiceTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.library.FrontendMicroservice.auth.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookDetailsController {

    private final BookService bookService;
    private final ExpectedBookService expectedBookService;
    private final JwtUtil jwtUtil;
    private final ReserveService reserveService;



    @GetMapping("/books/{bookId}")
    public String bookDetails(@PathVariable UUID bookId, Model model) {
        try {

            Book book = bookService.getBookById(bookId);

            BookDto booksByAuthor = bookService.getBooksByAuthor(book.getBookAuthor());
            BookDto booksByGenre = bookService.getBooksByGenre(book.getBookGenre());
            List<Book> authorBooks = booksByAuthor.getBooks();
            List<Book> similarBooks = booksByGenre.getBooks();
            if (jwtUtil.isAuthenticated()){
                String id = jwtUtil.getCurrentUserId();
                model.addAttribute("currentUserId", UUID.fromString(id));
            }
            model.addAttribute("book", book);
            model.addAttribute("authorBooks", authorBooks);
            model.addAttribute("similarBooks", similarBooks);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load book details");
        }

        return "book-details";
    }
    @PostMapping("/books/reserve")
    public ResponseEntity<HttpStatus> reserveBook(@RequestBody Reservation reservation){
        reserveService.reserveBook(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/expected-books/{bookId}")
    public String expectedBookDetails(@PathVariable UUID bookId, Model model) {
        try {

            ExpectedBook book = expectedBookService.getBookById(bookId);

            BookDto booksByAuthor = bookService.getBooksByAuthor(book.getExpectedBookAuthor());
            BookDto booksByGenre = bookService.getBooksByGenre(book.getExpectedBookGenre());
            List<Book> authorBooks = booksByAuthor.getBooks();
            List<Book> similarBooks = booksByGenre.getBooks();
            model.addAttribute("expectedBook", book);
            model.addAttribute("authorBooks", authorBooks);
            model.addAttribute("similarBooks", similarBooks);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load book details");
        }

        return "expected-book-details";
    }


}
