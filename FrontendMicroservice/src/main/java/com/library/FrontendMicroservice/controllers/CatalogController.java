package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.BookDto;
import com.library.FrontendMicroservice.dto.CategoriesDto;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Category;
import com.library.FrontendMicroservice.services.BookService;
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

    private final BookService bookService;

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "bookAddedAt") String sort,
            @RequestParam(value = "sortDir", defaultValue = "disc") String sortDir,
            Model model) {
        try {
            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            BookDto booksDto = bookService.getBooks(sort, page, genre, sortDir);
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPages();
            List<Book> books = booksDto.getBooks();

            model.addAttribute("categories", categories);
            model.addAttribute("books", books);
            model.addAttribute("selectedGenre", genre);
            model.addAttribute("sort", sort);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);


        } catch (Exception e) {
            log.error("Error loading catalog: {}", e.getMessage() + e.getCause());
            model.addAttribute("error", "Unable to load catalog");
            model.addAttribute("books", List.of());
            model.addAttribute("categories", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalBooks", 0);
        }

        return "catalog";
    }

}
