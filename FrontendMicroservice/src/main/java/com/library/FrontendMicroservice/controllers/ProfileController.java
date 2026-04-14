package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.auth.JwtCookieManager;
import com.library.FrontendMicroservice.auth.JwtUtil;
import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.AuthRequest;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Reservation;
import com.library.FrontendMicroservice.services.AuthService;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final JwtUtil jwtUtil;
    private final JwtCookieManager jwtCookieManager;
    private final UserService userService;
    private final AuthService authService;
    private final ReserveService reservationService;
    private final BookService bookService;
    private static final int PAGE_SIZE = 5;

    @GetMapping("/edit-credentials")
    public String editCredentialsPage(Model model) {
        try {
            UUID currentUserId = UUID.fromString(jwtUtil.getCurrentUserId());
            UserDTOForView userDTOForView = userService.getUserById(currentUserId);

            model.addAttribute("currentUsername", userDTOForView.getUsername());
            model.addAttribute("currentEmail", userDTOForView.getEmail());
            model.addAttribute("currentLibraryCode", userDTOForView.getLibraryCode());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to load page");
        }

        return "profile/edit-credentials";
    }

    @PostMapping("/edit-credentials")
    public String updateCredentials(@RequestBody ChangeCredentialDTO request,
                                    HttpServletResponse response) {
        try {
            String currentUserId = jwtUtil.getCurrentUserId();
            userService.updateCredentials(currentUserId, request);
            authService.logout();
            jwtCookieManager.clearJwtCookie(response);
            return "redirect:/login";

        } catch (Exception e) {
            return "redirect:/profile/edit-credentials?error=true";
        }
    }


    @GetMapping("/reservations")
    public String viewReservations(@RequestParam(defaultValue = "0") int page,
                                   Model model) {
        System.out.println("start");
        try {
            String id = jwtUtil.getCurrentUserId();
            ReservationsPageDto reservationsPage = reservationService.getUserReservations(id, page, PAGE_SIZE);
            System.out.println(reservationsPage.getTotalPages());
            List<ReservationDto> reservations = reservationsPage.getReservations();
            int totalPages = reservationsPage.getTotalPages();
            long totalReservations = reservationsPage.getTotalElements();
            List<ReservationDtoForView> reservationDtoForViews = getReservationsForView(reservations);

            model.addAttribute("reservations", reservationDtoForViews);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalReservations", totalReservations);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("error", "Unable to load reservations");
            model.addAttribute("reservations", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalReservations", 0);
            model.addAttribute("currentPage", 0);
        }

        return "profile/reservations";
    }

    private List<ReservationDtoForView> getReservationsForView(List<ReservationDto> reservations) {
        List<UUID> bookIds = reservations.stream()
                .map(ReservationDto::getReservationBook)
                .toList();
        BookDtoForReservations bookDtoForReservations = new BookDtoForReservations();
        bookDtoForReservations.setUuids(bookIds);
        BookDtoWithTotalElements bookDtoWithTotalElements = bookService.getBooksByIds(bookDtoForReservations);
        Map<UUID, Book> bookMap = bookDtoWithTotalElements.getBooks().stream()
                .collect(Collectors.toMap(Book::getBookId, Function.identity()));
        List<ReservationDtoForView> result = new ArrayList<>();
        for (ReservationDto reservation : reservations) {
            UUID bookId = reservation.getReservationBook();
            Book book = bookMap.get(bookId);
            if (book != null) {
                ReservationDtoForView dto = new ReservationDtoForView();
                dto.setId(reservation.getReservationId());
                dto.setBookAuthor(book.getBookAuthor());
                dto.setReservationDate(reservation.getReservationDate());
                dto.setStatus(reservation.getReservationStatus());
                dto.setBookId(reservation.getReservationBook());
                dto.setBookName(book.getBookName());
                dto.setBookImage(book.getBookImage());
                result.add(dto);
            }
        }

        return result;
    }

    @PostMapping("/reservations/{id}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@PathVariable UUID id) {
        System.out.println("Starting cancelling");
        try {
            System.out.println("Starting cancelling");
            reservationService.cancelReservation(id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
