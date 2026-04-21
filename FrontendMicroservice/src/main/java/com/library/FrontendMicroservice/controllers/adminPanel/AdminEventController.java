package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.AnnouncementDTO;
import com.library.FrontendMicroservice.dto.AnnouncementDTOForGetRequest;
import com.library.FrontendMicroservice.dto.AnnouncementDTOWithTotalElements;
import com.library.FrontendMicroservice.services.EventService;
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
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService adminEventService;

    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String listEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(required = false) String search,
                             Model model) {
        try {

            AnnouncementDTOWithTotalElements eventsPage = adminEventService.getAllEventsForAdmin(page, PAGE_SIZE, search);

            List<AnnouncementDTOForGetRequest> events = eventsPage.getAnnouncements();
            int totalPages = eventsPage.getAnnouncementPages();
            long totalEvents = eventsPage.getAnnouncementCount();

            model.addAttribute("events", events);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalEvents", totalEvents);
            model.addAttribute("searchQuery", search);


        } catch (Exception e) {
            model.addAttribute("error", "Unable to load events");
            model.addAttribute("events", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalEvents", 0);
            model.addAttribute("currentPage", 0);
        }

        return "admin-panel/admin-events";
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteEvent(@PathVariable UUID id) {
        try {
            adminEventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/create")
    public String createEventPage() {
        return "admin-panel/admin-create-event";
    }

    @PostMapping(value = "/create")
    public String createEvent(@Valid @ModelAttribute AnnouncementDTO announcementDTO,
                              BindingResult bindingResult,
                              @RequestParam(value = "coverImage", required = false) MultipartFile image,
                              RedirectAttributes redirectAttributes) {

        System.out.println(announcementDTO.getName() + " / " + announcementDTO.getType() + " / " + announcementDTO.getDate() + " / " + announcementDTO.getBody());

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            bindingResult.getAllErrors().forEach(error ->
                    errorMessage.append(error.getDefaultMessage()).append("; ")
            );
            redirectAttributes.addFlashAttribute("error", errorMessage.toString());
            redirectAttributes.addFlashAttribute("eventData", announcementDTO);
            return "redirect:/admin/events/create";
        }

        try {
            adminEventService.createEvent(announcementDTO, image);
            redirectAttributes.addFlashAttribute("success", "Event created successfully!");
            return "redirect:/admin/events";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to create event: " + e.getMessage());
            redirectAttributes.addFlashAttribute("eventData", announcementDTO);
            return "redirect:/admin/events/create";
        }
    }
    @GetMapping("/{id}/edit")
    public String editEventPage(@PathVariable UUID id, Model model) {
        try {
            AnnouncementDTOForGetRequest event = adminEventService.getEventById(id);
            model.addAttribute("event", event);
            return "admin-panel/admin-edit-event";
        } catch (Exception e) {
            return "redirect:/admin/events";
        }
    }

    @PostMapping(value = "/{id}/edit")
    public String editEvent(@PathVariable UUID id,
                            @Valid @ModelAttribute AnnouncementDTO announcementDTO,
                            BindingResult bindingResult,
                            @RequestParam(value = "coverImage", required = false) MultipartFile image,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/admin/events/" + id + "/edit";
        }

        try {
            adminEventService.updateEvent(id, announcementDTO, image);
            redirectAttributes.addFlashAttribute("success", "Event updated successfully!");
            return "redirect:/admin/events";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to update event: " + e.getMessage());
            return "redirect:/admin/events/" + id + "/edit";
        }
    }
}
