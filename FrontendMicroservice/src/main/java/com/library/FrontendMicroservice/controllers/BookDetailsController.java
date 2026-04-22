package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.BookDtoWithTotalElements;
import com.library.FrontendMicroservice.dto.ReportAvailabilityDTO;
import com.library.FrontendMicroservice.dto.UserDTO;
import com.library.FrontendMicroservice.dto.UserDTOForView;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
//import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.models.Reservation;
import com.library.FrontendMicroservice.services.*;
import jakarta.validation.Valid;
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
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookDetailsController {

    private final BookService bookService;
    private final ExpectedBookService expectedBookService;
    private final JwtUtil jwtUtil;
    private final ReserveService reserveService;
    private final ReportAvailabilityErrorService notificationService;
    private final UserService userService;


    @GetMapping("/books/{bookId}")
    public String bookDetails(@PathVariable UUID bookId, Model model) {
        try {

            Book book = bookService.getBookById(bookId);

            BookDtoWithTotalElements booksByAuthor = bookService.getBooksByAuthor(book.getBookAuthor());
            BookDtoWithTotalElements booksByGenre = bookService.getBooksByGenre(book.getBookGenre());
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

            BookDtoWithTotalElements booksByAuthor = bookService.getBooksByAuthor(book.getExpectedBookAuthor());
            BookDtoWithTotalElements booksByGenre = bookService.getBooksByGenre(book.getExpectedBookGenre());
            List<Book> authorBooks = booksByAuthor.getBooks();
            List<Book> similarBooks = booksByGenre.getBooks();

            if (jwtUtil.isAuthenticated()) {
                System.out.println("Auth");
                String currentUserId = jwtUtil.getCurrentUserId();
                UserDTOForView user = userService.getUserById(UUID.fromString(currentUserId));
                String currentUserEmail = user.getEmail();
                String currentUsername = user.getUsername();
                model.addAttribute("currentUserEmail", currentUserEmail);
                model.addAttribute("currentUsername", currentUsername);
                model.addAttribute("currentUserId", currentUserId);
            }
            model.addAttribute("expectedBook", book);
            model.addAttribute("authorBooks", authorBooks);
            model.addAttribute("similarBooks", similarBooks);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load book details");
        }

        return "expected-book-details";
    }

    @PostMapping("/expected-books/notify")
    public ResponseEntity<?> sendNotification(@Valid @RequestBody ReportAvailabilityDTO request) {
        try {
            notificationService.sendAvailabilityNotification(request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


}
