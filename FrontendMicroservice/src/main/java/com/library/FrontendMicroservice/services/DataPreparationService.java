package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataPreparationService {
    private final UserService userService;
    private final BookService bookService;

    public Map<UUID, UserDtoWithId> findUserData(List<ReservationDto> reservations){
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
        return userMap;
    }
    public Map<UUID, Book> findBookData(List<ReservationDto> reservations){
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
        return bookMap;
    }
}
