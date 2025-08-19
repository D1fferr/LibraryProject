package com.library.EvenService.services;


import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.models.Announcement;
import com.library.EvenService.repositories.AnnouncementRepository;
import com.library.EvenService.utill.AnnouncementNotFoundException;
import com.library.EvenService.utill.AnnouncementsNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    public AnnouncementService(AnnouncementRepository announcementRepository, ModelMapper modelMapper) {
        this.announcementRepository = announcementRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional(readOnly = true)
    public AnnouncementDTO findOneById (int id){
        return announcementRepository.findById(id).map(this::toDTO).orElseThrow(()
            -> new AnnouncementNotFoundException("Announcement not found"));
    }
    @Transactional(readOnly = true)
    public List<AnnouncementDTO> findAll(Pageable pageable){
        List<AnnouncementDTO> allAnnouncement = announcementRepository
                .findAll(pageable).map(this::toDTO).toList();
        if (allAnnouncement.isEmpty())
            throw new AnnouncementsNotFoundException("Announcements not found");
        return allAnnouncement;
    }
    @Transactional
    public void save(AnnouncementDTO announcementDTO){
        Announcement announcement = toEntity(announcementDTO);
        announcement.setCreatedDate(LocalDate.now());
        announcementRepository.save(announcement);
    }
    @Transactional
    public void  update (int id, AnnouncementDTO announcementDTO){
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found"));
        if (announcement.getBody()!=null)
            announcement.setBody(announcementDTO.getBody());
        if (announcement.getName()!=null)
            announcement.setName(announcementDTO.getName());
        if (announcement.getPhoto()!=null)
            announcement.setPhoto(announcementDTO.getPhoto());
        announcementRepository.save(announcement);
    }

    @Transactional
    public void delete(int id){
        announcementRepository.deleteById(id);
    }



    public Announcement toEntity(AnnouncementDTO announcementDTO){
        return modelMapper.map(announcementDTO, Announcement.class);
    }
    public AnnouncementDTO toDTO(Announcement announcement){
        return modelMapper.map(announcement, AnnouncementDTO.class);
    }
}
