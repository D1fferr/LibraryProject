package com.library.EvenService;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.models.Announcement;
import com.library.EvenService.repositories.AnnouncementRepository;
import com.library.EvenService.services.AnnouncementService;
import com.library.EvenService.services.ImageService;
import com.library.EvenService.utill.AnnouncementNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnnouncementServiceTest {


    @Mock
    private AnnouncementRepository repository;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile coverImage;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AnnouncementService service;

    @Test
    void save_AnnouncementWithoutImage_ShouldSave() {

        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setDate(LocalDate.now());
        dto.setBody("Test");
        dto.setName("Test");
        dto.setAddEvent(Boolean.TRUE);
        dto.setType("Test");

        Announcement announcement = new Announcement();
        when(modelMapper.map(dto, Announcement.class)).thenReturn(announcement);
        when(repository.save(any(Announcement.class))).thenReturn(announcement);

        service.save(dto, null);

        verify(repository, atLeastOnce()).save(any(Announcement.class));
        verify(imageService, never()).storeImage(any(), any());
    }

    @Test
    void save_AnnouncementWithImage_ShouldSaveWithImage() {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setDate(LocalDate.now());
        dto.setBody("Test");
        dto.setName("Test");
        dto.setAddEvent(Boolean.TRUE);
        dto.setType("Test");

        when(modelMapper.map(dto, Announcement.class)).thenReturn(new Announcement());
        when(coverImage.isEmpty()).thenReturn(false);
        when(repository.save(any())).thenReturn(new Announcement());
        when(imageService.storeImage(any(), any())).thenReturn("url");

        service.save(dto, coverImage);
        verify(imageService).storeImage(any(), any());
    }
    @Test
    void save_EmptyImage_ShouldNotProcessImage() {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setDate(LocalDate.now());
        dto.setBody("Test");
        dto.setName("Test");
        dto.setAddEvent(Boolean.TRUE);
        dto.setType("Test");

        Announcement announcement = new Announcement();
        when(modelMapper.map(dto, Announcement.class)).thenReturn(announcement);

        when(coverImage.isEmpty()).thenReturn(true);
        when(repository.save(any(Announcement.class))).thenReturn(announcement);

        service.save(dto, coverImage);

        verify(imageService, never()).storeImage(any(), any());
        verify(repository, atLeastOnce()).save(any(Announcement.class));
    }
    @Test
    void updateAnnouncement_AnnouncementExists_ShouldUpdate() {
        // Arrange
        UUID id = UUID.randomUUID();
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setDate(LocalDate.now());
        dto.setBody("Test");
        dto.setName("Test");
        dto.setAddEvent(Boolean.TRUE);
        dto.setType("Test");

        Announcement announcement = new Announcement();
        announcement.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(announcement));

        // Act
        service.update(id, dto, null);

        // Assert
        verify(repository).save(announcement);

    }
    @Test
    void updateAnnouncement_AnnouncementNotFound_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setDate(LocalDate.now());
        dto.setBody("Test");
        dto.setName("Test");
        dto.setAddEvent(Boolean.TRUE);
        dto.setType("Test");


        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AnnouncementNotFoundException.class, () -> {
            service.update(id, dto, null);
        });
    }
    @Test
    void updateAnnouncement_WithImage_ShouldUpdateImage() {
        // Arrange
        UUID id = UUID.randomUUID();
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setDate(LocalDate.now());
        dto.setBody("Test");
        dto.setName("Test");
        dto.setAddEvent(Boolean.TRUE);
        dto.setType("Test");

        Announcement announcement = new Announcement();
        announcement.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(announcement));
        when(coverImage.isEmpty()).thenReturn(false);
        when(imageService.storeImage(any(), any())).thenReturn("new_image_url");

        // Act
        service.update(id, dto, coverImage);

        // Assert
        verify(imageService).storeImage(coverImage, id);
    }

}
