package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.DataPreparationService;
import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class BookAdminController {
    private final BookService bookService;
    private final ReserveService reservationService;
    private final UserService userService;
    private final DataPreparationService dataPreparationService;

    @GetMapping("/create")
    public String createBookPage() {
        return "admin-panel/create-book";
    }

    @PostMapping(value = "/create")
    public String createBook(@ModelAttribute("bookData") @Valid BookDTOForCreate bookDTO,
                             BindingResult bindingResult,
                             @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("bookData", bookDTO);
            return "redirect:/admin/books/create";
        }
        try {

            bookService.createBook(bookDTO, coverImage);
            redirectAttributes.addFlashAttribute("success", "Book created successfully!");
            return "redirect:/admin/books";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create book: " + e.getMessage());
            redirectAttributes.addFlashAttribute("bookData", bookDTO);
            return "redirect:/admin/books/create";
        }
    }
    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String search,
                            Model model) {
        try {

            BookDtoWithTotalElements booksDto = bookService.getBooksForAdmin(page, search);
            long totalBooks = booksDto.getBookCount();
            int totalPages = booksDto.getBookPages();

            model.addAttribute("books", booksDto.getBooks());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalBooks", totalBooks);
            model.addAttribute("searchQuery", search);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load books");
            model.addAttribute("books", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalBooks", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/admin-books";
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/{id}/edit")
    public String editBookPage(@PathVariable UUID id, Model model) {
        try {
            Book book = bookService.getBookById(id);

            model.addAttribute("book", book);
            return "admin-panel/edit-book";
        } catch (Exception e) {
            return "redirect:/admin/books";
        }
    }

    @PostMapping(value = "/{id}/edit")
    public String editBook(@PathVariable UUID id,
                           @ModelAttribute("bookData") @Valid BookDto bookDTO,
                           BindingResult bindingResult,
                           @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                           RedirectAttributes redirectAttributes) {
        System.out.println("Start");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("bookData", bookDTO);
            return "redirect:/admin/books/" + id + "/edit";
        }

        try {
            bookService.updateBook(id, bookDTO, coverImage);
            redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
            return "redirect:/admin/books";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to update book: " + e.getMessage());
            return "redirect:/admin/books/" + id + "/edit";
        }
    }
    @GetMapping("/{id}/reservations")
    public String bookReservations(@PathVariable UUID id,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        try {

            Book book = bookService.getBookById(id);

            ReservationsPageDto reservationsPage = reservationService.getReservationsByBook(id, page, 5);

            List<ReservationDto> dtos = reservationsPage.getReservations();
            int totalPages = reservationsPage.getTotalPages();
            long totalReservations = reservationsPage.getTotalElements();
            List <ReservationForView> reservations = getReservationsForView(dtos);


            model.addAttribute("book", book);
            model.addAttribute("bookId", id);
            model.addAttribute("reservations", reservations);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalReservations", totalReservations);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load reservations");
            model.addAttribute("reservations", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalReservations", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/book-reservations";
    }

    private List<ReservationForView> getReservationsForView(List<ReservationDto> reservations) {

        Map<UUID, UserDtoWithId> userMap = dataPreparationService.findUserData(reservations);

        List<ReservationForView> result = new ArrayList<>();
        for (ReservationDto reservation : reservations) {
            UUID userId = reservation.getReservationUser();
            UserDtoWithId user = userMap.get(userId);
            if (user != null) {
                ReservationForView forView = new ReservationForView();
                forView.setReservationDate(reservation.getReservationDate());
                forView.setStatus(reservation.getReservationStatus());
                forView.setUsername(user.getUsername());
                forView.setId(reservation.getReservationId());
                result.add(forView);
            }
        }
        return result;
    }
}
