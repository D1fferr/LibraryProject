package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.services.BookService;
import com.library.FrontendMicroservice.services.ReserveService;
import com.library.FrontendMicroservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationsController {


    private final ReserveService adminReservationService;
    private final UserService userService;
    private final BookService bookService;
    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String allReservations(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(required = false) String search,
                                  Model model) {
        try {

            ReservationsPageDto reservationsPage = adminReservationService.getAllReservations(page, PAGE_SIZE, search);

            List<ReservationDto> dtos = reservationsPage.getReservations();
            int totalPages = reservationsPage.getTotalPages();
            long totalReservations = reservationsPage.getTotalElements();
            List<ReservationForViewForReservationPage> reservations = getDtoForAdmin(dtos);



            model.addAttribute("reservations", reservations);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalReservations", totalReservations);
            model.addAttribute("searchQuery", search);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("error", "Unable to load reservations");
            model.addAttribute("reservations", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalReservations", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/reservations";
    }
    private List<ReservationForViewForReservationPage> getDtoForAdmin(List<ReservationDto> reservations){

        List<UUID> userIds = reservations.stream()
                .map(ReservationDto::getReservationUser)
                .distinct()
                .toList();

        UserDtoForReservations userDTOForReservations = new UserDtoForReservations();
        userDTOForReservations.setUserDTOForViews(userIds);
        UserDtoWithListUsers userDTO = userService.getUsersById(userDTOForReservations, 0);
        Map<UUID, UserDtoWithId> userMap = userDTO.getUserDTOForViewList().stream()
                .collect(Collectors.toMap(UserDtoWithId::getId, Function.identity(),
                        (existing, replacement) -> existing));

        List<UUID> uniqueBookIds = reservations.stream()
                .map(ReservationDto::getReservationBook)
                .distinct()
                .toList();

        BookDtoForReservations bookDtoForReservations = new BookDtoForReservations();
        bookDtoForReservations.setUuids(uniqueBookIds);


        BookDtoWithTotalElements bookDtoWithTotalElements = bookService.getBooksByIds(bookDtoForReservations, 0);

        Map<UUID, Book> bookMap = bookDtoWithTotalElements.getBooks().stream()
                .collect(Collectors.toMap(
                        Book::getBookId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));


        List<ReservationForViewForReservationPage> result = new ArrayList<>();
        for (ReservationDto reservation : reservations) {
            Book book = bookMap.get(reservation.getReservationBook());
            UserDtoWithId user = userMap.get(reservation.getReservationUser());
            if (book != null && user!=null) {
                ReservationForViewForReservationPage dto = new ReservationForViewForReservationPage();
                dto.setId(reservation.getReservationId());
                dto.setStatus(reservation.getReservationStatus());
                dto.setBookName(book.getBookName());
                dto.setBookAuthor(book.getBookAuthor());
                dto.setReservationDate(reservation.getReservationDate());
                dto.setUsername(user.getUsername());
                result.add(dto);
            }
        }
        return result;


    }



}

