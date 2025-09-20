package com.library.EvenService.services;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.dto.EventDTO;
import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.models.Announcement;
import com.library.EvenService.models.News;
import com.library.EvenService.repositories.AnnouncementRepository;
import com.library.EvenService.utill.AnnouncementsNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    public EventService(AnnouncementRepository announcementRepository, ModelMapper modelMapper) {
        this.announcementRepository = announcementRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> findAllEvents(Pageable pageable){

        List<Announcement> allEvents = announcementRepository.findAllByAddEvent(true, pageable);
        if (allEvents.isEmpty())
            throw new AnnouncementsNotFoundException("Announcements not found");
        return allEvents.stream().map(this::toEventDTO).toList();
    }

    public EventDTO toEventDTO(Announcement announcement) {
        return modelMapper.map(announcement, EventDTO.class);
    }
}
