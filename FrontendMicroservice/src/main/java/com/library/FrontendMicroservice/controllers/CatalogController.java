package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.BookDtoWithTotalElements;
import com.library.FrontendMicroservice.dto.CategoriesDto;
import com.library.FrontendMicroservice.dto.ExpectedBookDtoWithTotalElements;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Category;
import com.library.FrontendMicroservice.models.ExpectedBook;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.ExpectedBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/catalog")
public class CatalogController {

    private final BookService bookService;
    private final ExpectedBookService expectedBookService;
    @GetMapping()
    public String viewCatalog(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sortBy", defaultValue = "bookAddedAt") String sort,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            Model model) {
        try {
            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            BookDtoWithTotalElements booksDto = bookService.getBooks(search, sort, page, genre, sortDir);
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
    @GetMapping("/most-popular-books")
    public String viewMostPopularBooks(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "bookAddedAt") String sort,
            Model model) {
        try {
            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            BookDtoWithTotalElements booksDto = bookService.getMostPopularBooks();
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

        return "most-popular-books";
    }
    @GetMapping("/upcoming-books")
    public String viewUpcomingBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "expectedBookAddedAt") String sort,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        try {
            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            ExpectedBookDtoWithTotalElements booksDto = expectedBookService.getAllExpectedBooks(page, sort, search);
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPage();
            List<ExpectedBook> books = booksDto.getExpectedBooks();

            model.addAttribute("categories", categories);
            model.addAttribute("books", books);
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

        return "upcoming-books";
    }





}
