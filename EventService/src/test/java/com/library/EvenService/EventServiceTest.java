package com.library.EvenService;

import com.library.EvenService.dto.EventDTO;
import com.library.EvenService.models.Announcement;
import com.library.EvenService.repositories.AnnouncementRepository;
import com.library.EvenService.services.EventService;
import com.library.EvenService.utill.AnnouncementsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {


    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private EventService eventService;

    @Test
    void findAllEvents_EventsExist_ShouldReturnList() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        List<Announcement> events = List.of(new Announcement(), new Announcement());

        when(announcementRepository.findAllByAddEvent(true, pageable)).thenReturn(events);

        // Act
        List<EventDTO> result = eventService.findAllEvents(pageable);

        // Assert
        verify(announcementRepository).findAllByAddEvent(true, pageable);
        assertNotNull(result);
    }

    @Test
    void findAllEvents_NoEvents_ShouldThrowException() {
        // Arrange
        Pageable pageable = mock(Pageable.class);

        when(announcementRepository.findAllByAddEvent(true, pageable))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(AnnouncementsNotFoundException.class, () -> {
            eventService.findAllEvents(pageable);
        });
    }

    @Test
    void findAllEvents_VerifyQueryParameter() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        when(announcementRepository.findAllByAddEvent(true, pageable))
                .thenReturn(List.of(new Announcement()));

        // Act
        eventService.findAllEvents(pageable);

        // Assert
        verify(announcementRepository).findAllByAddEvent(true, pageable);
    }
}
