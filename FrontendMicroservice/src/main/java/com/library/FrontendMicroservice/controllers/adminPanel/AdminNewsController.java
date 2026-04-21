package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.NewsDTO;
import com.library.FrontendMicroservice.dto.NewsDTOForGetRequest;
import com.library.FrontendMicroservice.dto.NewsDtoWithTotalElements;
import com.library.FrontendMicroservice.services.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/news")
public class AdminNewsController {
    private final NewsService adminNewsService;
    private static final int PAGE_SIZE = 10;
    @GetMapping("/create")
    public String createNewsPage() {
        return "admin-panel/admin-create-news";
    }

    @PostMapping(value = "/create")
    public String createNews(@Valid @ModelAttribute NewsDTO newsDTO,
                             BindingResult bindingResult,
                             @RequestParam(value = "coverImage", required = false) MultipartFile image,
                             RedirectAttributes redirectAttributes) {
        System.out.println(newsDTO.getName() + " / " + newsDTO.getBody());
        if (bindingResult.hasErrors()) {

            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append("; ")
            );

            redirectAttributes.addFlashAttribute("error", errorMessage.toString());
            redirectAttributes.addFlashAttribute("newsData", newsDTO);
            return "redirect:/admin/news/create";
        }
        try {
            adminNewsService.createNews(newsDTO, image);

            redirectAttributes.addFlashAttribute("success", "News created successfully!");
            return "redirect:/admin/news";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to create news: " + e.getMessage());
            redirectAttributes.addFlashAttribute("newsData", newsDTO);
            return "redirect:/admin/news/create";
        }
    }
    @GetMapping
    public String listNews(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String search,
                           Model model) {
        try {

            NewsDtoWithTotalElements newsPage = adminNewsService.getAllNewsForAdmin(page, PAGE_SIZE, search);

            List<NewsDTOForGetRequest> news = newsPage.getNews();
            int totalPages = newsPage.getNewsPages();
            long totalNews = newsPage.getNewsCount();

            model.addAttribute("news", news);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalNews", totalNews);
            model.addAttribute("searchQuery", search);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load news");
            model.addAttribute("news", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalNews", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/admin-news";
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteNews(@PathVariable UUID id) {
        try {
            adminNewsService.deleteNews(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/{id}/edit")
    public String editNewsPage(@PathVariable UUID id, Model model) {
        try {
            NewsDTOForGetRequest news = adminNewsService.getNewsById(id);
            model.addAttribute("news", news);
            return "admin-panel/admin-edit-news";
        } catch (Exception e) {
            return "redirect:/admin/news";
        }
    }

    @PostMapping(value = "/{id}/edit")
    public String editNews(@PathVariable UUID id,
                           @Valid @ModelAttribute NewsDTO newsDTO,
                           BindingResult bindingResult,
                           @RequestParam(value = "coverImage", required = false) MultipartFile image,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/admin/news/" + id + "/edit";
        }

        try {
            adminNewsService.updateNews(id, newsDTO, image);

            redirectAttributes.addFlashAttribute("success", "News updated successfully!");
            return "redirect:/admin/news";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to update news: " + e.getMessage());
            return "redirect:/admin/news/" + id + "/edit";
        }
    }



}
