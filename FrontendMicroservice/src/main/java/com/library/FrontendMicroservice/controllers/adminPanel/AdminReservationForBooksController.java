package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.ReservationCancellationNotificationDTO;
import com.library.FrontendMicroservice.services.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reservations")
public class AdminReservationForBooksController {
    private final ReserveService adminReservationService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable UUID id) {

            adminReservationService.confirmReservation(id);
            return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable UUID id,
                                               @RequestBody ReservationCancellationNotificationDTO request) {
            adminReservationService.cancelReservationForBook(id, request);
            return ResponseEntity.ok().build();

    }
}
