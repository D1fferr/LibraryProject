package com.library.EvenService.services;


import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.models.Announcement;
import com.library.EvenService.repositories.AnnouncementRepository;
import com.library.EvenService.utill.AnnouncementNotFoundException;
import com.library.EvenService.utill.AnnouncementsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public AnnouncementDTO findOneById (int id){
        log.info("Trying to find one announcement. ID: '{}'", id);
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        if (optionalAnnouncement.isEmpty()){
            log.info("The announcement not found. ID: '{}'", id);
            throw new AnnouncementNotFoundException("Announcement not found");
        }
        log.info("The announcement found. ID: '{}'", id);
        return toDTO(optionalAnnouncement.get());
    }
    @Transactional(readOnly = true)
    public List<AnnouncementDTO> findAll(Pageable pageable){
        log.info("Trying to find all announcement");
        Page<Announcement> allAnnouncement = announcementRepository.findAll(pageable);
        if (allAnnouncement.isEmpty()){
            log.info("Announcements not found");
            throw new AnnouncementsNotFoundException("Announcements not found");
        }
        log.info("All announcement found");
        return allAnnouncement.stream().map(this::toDTO).toList();
    }
    @Transactional
    public void save(AnnouncementDTO announcementDTO){
        Announcement announcement = toEntity(announcementDTO);
        announcement.setCreatedDate(LocalDate.now());
        announcementRepository.save(announcement);
        log.info("The announcement saved. ID: '{}'", announcement.getId());
    }
    @Transactional
    public void  update (int id, AnnouncementDTO announcementDTO){
        log.info("Trying to find the announcement for update. ID: '{}'", id);
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(id);
        if (optionalAnnouncement.isEmpty()){
            log.info("The announcement for update not found. ID: '{}'", id);
            throw new AnnouncementNotFoundException("Announcement not found");
        }
        log.info("Starting of updating the announcement. ID: '{}'", id);
        Announcement announcement = optionalAnnouncement.get();
        if (announcement.getBody()!=null)
            announcement.setBody(announcementDTO.getBody());
        if (announcement.getName()!=null)
            announcement.setName(announcementDTO.getName());
        if (announcement.getPhoto()!=null)
            announcement.setPhoto(announcementDTO.getPhoto());
        announcementRepository.save(announcement);
        log.info("The announcement updated. ID: '{}'", id);
    }

    @Transactional
    public void delete(int id){
        announcementRepository.deleteById(id);
        log.info("The announcement deleted. ID: '{}'", id);
    }

    private Announcement toEntity(AnnouncementDTO announcementDTO){
        log.info("Mapping the announcementDTO to entity. Name: '{}'", announcementDTO.getName());
        return modelMapper.map(announcementDTO, Announcement.class);
    }
    private AnnouncementDTO toDTO(Announcement announcement){
        log.info("Mapping the announcement to DTO. ID: '{}'", announcement.getId());
        return modelMapper.map(announcement, AnnouncementDTO.class);
    }
}
