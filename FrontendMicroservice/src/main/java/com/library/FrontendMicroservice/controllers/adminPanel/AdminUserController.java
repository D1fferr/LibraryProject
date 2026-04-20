package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.DataPreparationService;
import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.services.UserService;
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
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService adminUserService;
    private final ReserveService adminReservationService;
    private final BookService bookService;
    private final DataPreparationService dataPreparationService;
    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String allUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String search,
                           Model model) {
        try {

            UserPageDto usersPage = adminUserService.getAllUsers(page, PAGE_SIZE, search);

            List<UserDtoWithId> users = usersPage.getUsers();
            int totalPages = usersPage.getTotalPages();
            long totalUsers = usersPage.getTotalElements();

            model.addAttribute("users", users);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("searchQuery", search);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load users");
            model.addAttribute("users", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalUsers", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/users";
    }

    @PutMapping("/{id}/library-code")
    @ResponseBody
    public ResponseEntity<?> updateLibraryCode(@PathVariable UUID id,
                                               @RequestBody UserDTOForChangeProfile request) {
        try {

            adminUserService.updateLibraryCode(id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/{id}/reservations")
    public String userReservations(@PathVariable String id,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        try {

            UserDTOForView user = adminUserService.getUserById(UUID.fromString(id));

            ReservationsPageDto reservationsPage = adminReservationService.getUserReservations(id, page, PAGE_SIZE);

            List<ReservationDto> dtos = reservationsPage.getReservations();
            int totalPages = reservationsPage.getTotalPages();
            long totalReservations = reservationsPage.getTotalElements();
            List<ReservationForViewForAdmin> reservations = getReservationsForView(dtos);
            model.addAttribute("user", user);
            model.addAttribute("userId", id);
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

        return "admin-panel/user-reservations";
    }
    private List<ReservationForViewForAdmin> getReservationsForView(List<ReservationDto> reservations) {
        Map<UUID, Book> bookMap = dataPreparationService.findBookData(reservations);
        List<ReservationForViewForAdmin> result = new ArrayList<>();
        for (ReservationDto reservation : reservations) {
            Book book = bookMap.get(reservation.getReservationBook());
            if (book != null) {
                ReservationForViewForAdmin dto = new ReservationForViewForAdmin();
                dto.setId(reservation.getReservationId());
                dto.setBookAuthor(book.getBookAuthor());
                dto.setReservationDate(reservation.getReservationDate());
                dto.setStatus(reservation.getReservationStatus());
                dto.setBookName(book.getBookName());
                result.add(dto);
            }
        }
        return result;
    }
}
