package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.NewsDTOForGetRequest;
import com.library.FrontendMicroservice.dto.NewsDtoWithTotalElements;
import com.library.FrontendMicroservice.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;
    @GetMapping
    public String news(@RequestParam(defaultValue = "0") int page,
                       Model model) {
         NewsDtoWithTotalElements newsPage = newsService.getAllNews(page);

            List<NewsDTOForGetRequest> news = newsPage.getNews();
            int totalPages = newsPage.getNewsPages();
            long totalNews = newsPage.getNewsCount();

            model.addAttribute("news", news);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalNews", totalNews);

        return "news";
    }

    @GetMapping("/{id}")
    public String newsDetails(@PathVariable UUID id, Model model) {
            NewsDTOForGetRequest news = newsService.getNewsById(id);
            model.addAttribute("news", news);

        return "news-details";
    }

}
