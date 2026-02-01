package com.library.EvenService;

import com.library.EvenService.dto.NewsDTO;
import com.library.EvenService.models.News;
import com.library.EvenService.repositories.NewsRepository;
import com.library.EvenService.services.ImageService;
import com.library.EvenService.services.NewsService;
import com.library.EvenService.utill.NewsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {
    @Mock
    private NewsRepository repository;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile coverImage;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NewsService service;

    @Test
    void save_NewsWithoutImage_ShouldSave() {

        NewsDTO dto = new NewsDTO();
        dto.setBody("Test");
        dto.setName("Test");

        News news = new News();
        when(modelMapper.map(dto, News.class)).thenReturn(news);
        when(repository.save(any(News.class))).thenReturn(news);

        service.save(dto, null);

        verify(repository, atLeastOnce()).save(any(News.class));
        verify(imageService, never()).storeImage(any(), any());
    }

    @Test
    void save_NewsWithImage_ShouldSaveWithImage() {
        NewsDTO dto = new NewsDTO();
        dto.setBody("Test");
        dto.setName("Test");

        when(modelMapper.map(dto, News.class)).thenReturn(new News());
        when(coverImage.isEmpty()).thenReturn(false);
        when(repository.save(any())).thenReturn(new News());
        when(imageService.storeImage(any(), any())).thenReturn("url");

        service.save(dto, coverImage);
        verify(imageService).storeImage(any(), any());
    }
    @Test
    void save_EmptyImage_ShouldNotProcessImage() {
        NewsDTO dto = new NewsDTO();
        dto.setBody("Test");
        dto.setName("Test");

        News news = new News();
        when(modelMapper.map(dto, News.class)).thenReturn(news);

        when(coverImage.isEmpty()).thenReturn(true);
        when(repository.save(any(News.class))).thenReturn(news);

        service.save(dto, coverImage);

        verify(imageService, never()).storeImage(any(), any());
        verify(repository, atLeastOnce()).save(any(News.class));
    }
    @Test
    void updateNews_NewsExists_ShouldUpdate() {
        // Arrange
        UUID id = UUID.randomUUID();
        NewsDTO dto = new NewsDTO();
        dto.setBody("Test");
        dto.setName("Test");

        News news = new News();
        news.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(news));

        // Act
        service.update(id, dto, null);

        // Assert
        verify(repository).save(news);

    }
    @Test
    void updateNews_NewsNotFound_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        NewsDTO dto = new NewsDTO();
        dto.setBody("Test");
        dto.setName("Test");


        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NewsNotFoundException.class, () -> {
            service.update(id, dto, null);
        });
    }
    @Test
    void updateNews_WithImage_ShouldUpdateImage() {
        // Arrange
        UUID id = UUID.randomUUID();
        NewsDTO dto = new NewsDTO();
        dto.setBody("Test");
        dto.setName("Test");

        News news = new News();
        news.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(news));
        when(coverImage.isEmpty()).thenReturn(false);
        when(imageService.storeImage(any(), any())).thenReturn("new_image_url");

        // Act
        service.update(id, dto, coverImage);

        // Assert
        verify(imageService).storeImage(coverImage, id);
    }
}
