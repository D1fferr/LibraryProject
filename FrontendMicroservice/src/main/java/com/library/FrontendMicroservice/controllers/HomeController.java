package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.BookDtoWithTotalElements;
import com.library.FrontendMicroservice.dto.ExpectedBookDtoWithTotalElements;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.ExpectedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BookService bookService;
    private final ExpectedBookService expectedBookService;

    @GetMapping({"/", "/home"})
    public String home(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "sortBy", defaultValue = "bookAddedAt", required = false) String sort,
            @RequestParam(value = "sortDir", defaultValue = "disc", required = false) String sortDir,
            Model model) {
        try {

                    BookDtoWithTotalElements popularBooksDto = bookService.getMostPopularBooks();
                    List<Book> popularBooks = popularBooksDto.getBooks();
                    model.addAttribute("popularBooks", popularBooks);

                    BookDtoWithTotalElements newArrivalsDto = bookService.getRecentlyAddedAt();
                    List<Book> newArrivals = newArrivalsDto.getBooks();
                    model.addAttribute("newArrivals", newArrivals);

                    ExpectedBookDtoWithTotalElements upcomingBooksDto = expectedBookService.getExpectedBooks();
                    List<ExpectedBook> upcomingBooks = upcomingBooksDto.getExpectedBooks();
                    model.addAttribute("upcomingBooks", upcomingBooks);

                    model.addAttribute("categories", Arrays.asList(
                            "All Books", "Fiction", "Non-Fiction", "Children's", "New Arrivals"
                    ));
                    return "home";
    }catch (Exception e){
            System.out.println(e.getMessage() + e.getCause());
            return null;
        }
    }
}
