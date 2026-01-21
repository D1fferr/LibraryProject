package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.dto.BookDTOForKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final BookService bookService;
    private final MostPopularBooksCounterService counterService;

    @KafkaListener(topics = "expected-book-topic", groupId = "catalog-service",
            containerFactory = "bookConcurrentKafkaListenerContainerFactory")
    public void addExpectedBookToCurrentBook(BookDTOForKafka book){
        log.info("Saving the expected book to current book. ID: '{}'", book.getBookId());
        bookService.saveExpectedBookToCurrentBook(book);
    }
    @KafkaListener(topics = "reservations", groupId = "catalog-service",
            containerFactory = "reservationConcurrentKafkaListenerContainerFactory")
    public void addToMostPopularBook(String id){
        log.info("Enhancement counter for the most popular books");
        counterService.update(id);
    }


}
