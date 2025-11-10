package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.dto.BookDTOForKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final BookService bookService;
    private final MostPopularBooksCounterService counterService;

    @KafkaListener(topics = "expected-book-topic", groupId = "catalog-service",
            containerFactory = "bookConcurrentKafkaListenerContainerFactory")
    public void addExpectedBookToCurrentBook(BookDTOForKafka book){
        bookService.saveExpectedBookToCurrentBook(book);
    }
    @KafkaListener(topics = "reservations", groupId = "catalog-service",
            containerFactory = "reservationConcurrentKafkaListenerContainerFactory")
    public void addToMostPopularBook(String id){
        counterService.update(id);
    }


}
