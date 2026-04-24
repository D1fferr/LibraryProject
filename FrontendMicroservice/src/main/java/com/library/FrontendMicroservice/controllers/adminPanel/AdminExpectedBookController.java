package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.ExpectedBook;
import com.library.FrontendMicroservice.services.ExpectedBookService;
import com.library.FrontendMicroservice.services.ReportAvailabilityErrorService;
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
@RequestMapping("/admin/expected-books")
public class AdminExpectedBookController {
    private final ExpectedBookService adminExpectedBookService;
    private final ReportAvailabilityErrorService reportAvailabilityErrorService;
    private static final int PAGE_SIZE = 12;
    private static final int REPORT_PAGE_SIZE = 10;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String search,
                            Model model) {
        try {

            ExpectedBookDtoWithTotalElements booksPage = adminExpectedBookService.getBooksForAdmin(page, PAGE_SIZE, search);

            List<ExpectedBook> books = booksPage.getExpectedBooks();
            int totalPages = booksPage.getBookPage();
            long totalBooks = booksPage.getBookCount();

            model.addAttribute("books", books);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);
            model.addAttribute("searchQuery", search);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load expected books");
            model.addAttribute("books", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalBooks", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/admin-expected-books";
    }

    @GetMapping("/create")
    public String createExpectedBookPage() {
        return "admin-panel/admin-create-expected-book";
    }

    @PostMapping( "/create")
    public String createExpectedBook(@Valid @ModelAttribute ExpectedBookDTOCreate expectedBookDTO,
                                     BindingResult bindingResult,
                                     @RequestParam(value = "coverImage", required = false) MultipartFile image,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append("; ")
            );
            redirectAttributes.addFlashAttribute("error", errorMessage.toString());
            redirectAttributes.addFlashAttribute("bookData", expectedBookDTO);
            return "redirect:/admin/expected-books/create";
        }

        try {
            adminExpectedBookService.createBook(expectedBookDTO, image);
            redirectAttributes.addFlashAttribute("success", "Expected book created successfully!");
            return "redirect:/admin/expected-books";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to create expected book: " + e.getMessage());
            redirectAttributes.addFlashAttribute("bookData", expectedBookDTO);
            return "redirect:/admin/expected-books/create";
        }
    }
    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
        try {
            adminExpectedBookService.deleteBook(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/add-to-current")
    @ResponseBody
    public ResponseEntity<?> addToCurrentBooks(@PathVariable UUID id) {
        try {
            adminExpectedBookService.addToCurrentBooks(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/{id}/edit")
    public String editBookPage(@PathVariable UUID id, Model model) {
        try {
            ExpectedBook book = adminExpectedBookService.getBookById(id);
            model.addAttribute("book", book);
            return "admin-panel/admin-edit-expected-book";
        } catch (Exception e) {
            return "redirect:/admin/expected-books";
        }
    }

    @PostMapping(value = "/{id}/edit")
    public String editBook(@PathVariable UUID id,
                           @Valid @ModelAttribute ExpectedBookDTOCreate bookDTO,
                           BindingResult bindingResult,
                           @RequestParam(value = "coverImage", required = false) MultipartFile image,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/admin/expected-books/" + id + "/edit";
        }

        try {
            adminExpectedBookService.updateBook(id, bookDTO, image);
            redirectAttributes.addFlashAttribute("success", "Expected book updated successfully!");
            return "redirect:/admin/expected-books";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update expected book: " + e.getMessage());
            return "redirect:/admin/expected-books/" + id + "/edit";
        }
    }
    @GetMapping("/report-error")
    public String reportErrorPage(@RequestParam(defaultValue = "0") int page,
                                  Model model) {
        try {

            PageReportAvailabilityErrorDTO reportsPage = reportAvailabilityErrorService.getErrors(page, REPORT_PAGE_SIZE);

            List<ReportAvailabilityErrorDTO> reports = reportsPage.getDtoList();
            int totalPages = reportsPage.getTotalPages();
            int totalReports = reportsPage.getTotalElements();

            model.addAttribute("reports", reports);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalReports", totalReports);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load reports");
            model.addAttribute("reports", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalReports", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/admin-report-error";
    }

    @PostMapping("/report-error/send")
    @ResponseBody
    public ResponseEntity<?> sendNotification(@Valid @RequestBody ReportAvailabilityErrorDTOWithId request) {
        try {
            reportAvailabilityErrorService.sendNotification(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
