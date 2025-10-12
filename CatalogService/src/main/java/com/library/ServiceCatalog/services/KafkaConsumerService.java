package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.models.Book;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final BookService bookService;

    public KafkaConsumerService(BookService bookService) {
        this.bookService = bookService;
    }

    @KafkaListener(topics = "expected-book-topic", groupId = "catalog-service",containerFactory = "bookConcurrentKafkaListenerContainerFactory")
    public void addExpectedBookToCurrentBook(Book book){
        bookService.saveExpectedBookToCurrentBook(book);
    }

}
