package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.dto.AnnouncementDTOForGetRequest;
import com.library.FrontendMicroservice.dto.AnnouncementDTOWithTotalElements;
import com.library.FrontendMicroservice.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping()
    public String events(@RequestParam(defaultValue = "0") int page,
                         Model model) {
        try {

            AnnouncementDTOWithTotalElements eventsPage = eventService.getAllEvents(page);

            List<AnnouncementDTOForGetRequest> events = eventsPage.getAnnouncements();
            int totalPages = eventsPage.getAnnouncementPages();
            long totalEvents = eventsPage.getAnnouncementCount();

            model.addAttribute("events", events);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalEvents", totalEvents);

        } catch (Exception e) {
            model.addAttribute("error", "Unable to load events");
            model.addAttribute("events", List.of());
            model.addAttribute("totalPages", 0);
            model.addAttribute("totalEvents", 0);
            model.addAttribute("currentPage", 0);
        }

        return "events";
    }
    @GetMapping("/{id}")
    public String eventDetails(@PathVariable UUID id, Model model) {
        try {

            AnnouncementDTOForGetRequest event = eventService.getEventById(id);
            model.addAttribute("event", event);

        } catch (Exception e) {
            model.addAttribute("error", "Unable to load event details");
        }

        return "event-details";
    }
}
