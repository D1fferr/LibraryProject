package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.models.MostPopularBooksCounter;
import com.library.ServiceCatalog.repositories.MostPopularBooksCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MostPopularBooksCounterService {

    private final MostPopularBooksCounterRepository mostPopularBooksCounterRepository;


    @Transactional
    public void update(String id){

        Optional<MostPopularBooksCounter> booksCounter = mostPopularBooksCounterRepository.findById(UUID.fromString(id));
        if (booksCounter.isEmpty()){
            MostPopularBooksCounter mostPopularBooksCounter = new MostPopularBooksCounter();
            mostPopularBooksCounter.setBookId(UUID.fromString(id));
            mostPopularBooksCounter.setCreatedAt(LocalDateTime.now());
            mostPopularBooksCounter.setCounter(1);
            mostPopularBooksCounterRepository.save(mostPopularBooksCounter);
        }else {
            booksCounter.get().setCounter(booksCounter.get().getCounter() + 1);
            mostPopularBooksCounterRepository.save(booksCounter.get());
        }
    }
}
