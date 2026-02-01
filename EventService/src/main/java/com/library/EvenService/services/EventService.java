package com.library.EvenService.services;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.dto.EventDTO;
import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.models.Announcement;
import com.library.EvenService.models.News;
import com.library.EvenService.repositories.AnnouncementRepository;
import com.library.EvenService.utill.AnnouncementsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<EventDTO> findAllEvents(Pageable pageable){
        List<Announcement> allEvents = announcementRepository.findAllByAddEvent(true, pageable);
        if (allEvents.isEmpty()){
            log.warn("Announcements for events not found");
            throw new AnnouncementsNotFoundException("Announcements not found");
        }
        log.info("All events found");
        return allEvents.stream().map(this::toEventDTO).toList();
    }

    public EventDTO toEventDTO(Announcement announcement) {
        log.info("Mapping the announcement to eventDTO. ID: '{}'", announcement.getId());
        return modelMapper.map(announcement, EventDTO.class);
    }
}
