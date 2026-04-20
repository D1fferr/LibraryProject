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
        try {
            adminReservationService.confirmReservation(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable UUID id,
                                               @RequestBody ReservationCancellationNotificationDTO request) {
        try {
            adminReservationService.cancelReservationForBook(id, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
