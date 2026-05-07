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
            @RequestParam(value = "sortBy", defaultValue = "bookAddedAt") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            Model model) {

            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            BookDtoWithTotalElements booksDto = bookService.getBooks(search, sortBy, page, genre, sortDir);
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPages();
            List<Book> books = booksDto.getBooks();

            model.addAttribute("categories", categories);
            model.addAttribute("books", books);
            model.addAttribute("selectedGenre", genre);
            model.addAttribute("sort", sortBy);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);


        return "catalog";
    }
    @GetMapping("/most-popular-books")
    public String viewMostPopularBooks(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "bookAddedAt") String sortBy,
            Model model) {

            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            BookDtoWithTotalElements booksDto = bookService.getMostPopularBooks();
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPages();
            List<Book> books = booksDto.getBooks();

            model.addAttribute("categories", categories);
            model.addAttribute("books", books);
            model.addAttribute("selectedGenre", genre);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);
            model.addAttribute("currentPageName", "most-popular");


        return "most-popular-books";
    }
    @GetMapping("/upcoming-books")
    public String viewUpcomingBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "expectedBookAddedAt") String sortBy,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
            CategoriesDto categoriesDto = bookService.getAllCategories();
            List<Category> categories = categoriesDto.getCategories();
            ExpectedBookDtoWithTotalElements booksDto = expectedBookService.getAllExpectedBooks(page, sortBy, search);
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPage();
            List<ExpectedBook> books = booksDto.getExpectedBooks();

            model.addAttribute("categories", categories);
            model.addAttribute("books", books);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);
            model.addAttribute("currentPageName", "upcoming");

        return "upcoming-books";
    }





}
