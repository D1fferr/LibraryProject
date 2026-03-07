package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.BookDto;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Category;
import com.library.FrontendMicroservice.testclasses.BookServiceTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

    private final BookServiceTest bookService;

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "bookAddedAt") String sort,
            Model model) {

        try {
            List<Category> categories = bookService.getAllCategories();

            BookDto booksDto = bookService.getBooks(genre, page, sort);
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPage();
            List<Book> books = booksDto.getBooks();

            model.addAttribute("categories", categories);
            model.addAttribute("books", books);
            model.addAttribute("selectedGenre", genre);
            model.addAttribute("sort", sort);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);


        } catch (Exception e) {
            log.error("Error loading catalog: {}", e.getMessage());
            model.addAttribute("error", "Unable to load catalog");
            model.addAttribute("books", List.of());
            model.addAttribute("categories", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalBooks", 0);
        }

        return "catalog";
    }

}
