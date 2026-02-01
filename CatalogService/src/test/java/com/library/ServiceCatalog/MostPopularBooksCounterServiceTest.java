package com.library.ServiceCatalog;

import com.library.ServiceCatalog.models.MostPopularBooksCounter;
import com.library.ServiceCatalog.repositories.MostPopularBooksCounterRepository;
import com.library.ServiceCatalog.services.MostPopularBooksCounterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MostPopularBooksCounterServiceTest {
    @Mock
    private MostPopularBooksCounterRepository mostPopularBooksCounterRepository;

    @InjectMocks
    private MostPopularBooksCounterService mostPopularBooksCounterService;

    @Test
    void update_CounterExists_ShouldIncrementCounter() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        UUID uuid = UUID.fromString(id);

        MostPopularBooksCounter existingCounter = new MostPopularBooksCounter();
        existingCounter.setCounter(5);

        when(mostPopularBooksCounterRepository.findById(uuid))
                .thenReturn(Optional.of(existingCounter));

        // Act
        mostPopularBooksCounterService.update(id);

        // Assert
        verify(mostPopularBooksCounterRepository).save(existingCounter);
        assertEquals(6, existingCounter.getCounter());
    }

    @Test
    void update_CounterNotExists_ShouldCreateNewCounter() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";

        when(mostPopularBooksCounterRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        // Act
        mostPopularBooksCounterService.update(id);

        // Assert
        verify(mostPopularBooksCounterRepository).save(any(MostPopularBooksCounter.class));
    }

    @Test
    void update_NewCounter_ShouldHaveCounterOne() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";

        when(mostPopularBooksCounterRepository.findById(any()))
                .thenReturn(Optional.empty());

        // Act
        mostPopularBooksCounterService.update(id);

        // Assert
        verify(mostPopularBooksCounterRepository).save(any());
    }

}
