package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.auth.FeignErrorHandler;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
import com.library.FrontendMicroservice.testclasses.BookServiceTest;
import com.library.FrontendMicroservice.testclasses.ExpectedBookServiceTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BookServiceTest bookService;
    private final ExpectedBookServiceTest expectedBookService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        return FeignErrorHandler.handleRequest(() -> {
                    List<Book> popularBooks = bookService.getMostPopularBooks();
                    model.addAttribute("popularBooks", popularBooks);

                    List<Book> newArrivals = bookService.getRecentlyAddedAt();
                    model.addAttribute("newArrivals", newArrivals);

                    List<ExpectedBook> upcomingBooks = expectedBookService.getExpectedBooks();
                    model.addAttribute("upcomingBooks", upcomingBooks);

                    model.addAttribute("categories", Arrays.asList(
                            "All Books", "Fiction", "Non-Fiction", "Children's", "New Arrivals"
                    ));
                    return "home";
                },
                model);
    }
}
