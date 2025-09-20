package com.library.EvenService.controllers;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.dto.EventDTO;
import com.library.EvenService.services.AnnouncementService;
import com.library.EvenService.services.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventDTO>> getAllEvents(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "eventPerPage", defaultValue = "5") Integer eventPerPage){
        return new ResponseEntity<>(eventService.findAllEvents(PageRequest.of(page, eventPerPage)), HttpStatus.OK);
    }


}
