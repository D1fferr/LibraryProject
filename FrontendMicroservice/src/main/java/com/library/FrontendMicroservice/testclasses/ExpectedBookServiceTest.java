package com.library.FrontendMicroservice.testclasses;

import com.library.FrontendMicroservice.dto.ExpectedBookDtoWithTotalElements;
import com.library.FrontendMicroservice.models.ExpectedBook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ExpectedBookServiceTest {
    public ExpectedBookDtoWithTotalElements getExpectedBooks(){
        ExpectedBook book1 = createBook(1);
        ExpectedBook book2 = createBook(2);
        ExpectedBook book3 = createBook(3);
        ExpectedBook book4 = createBook(4);
        List<ExpectedBook> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        ExpectedBookDtoWithTotalElements expectedBookDtoWithTotalElements = new ExpectedBookDtoWithTotalElements();
        expectedBookDtoWithTotalElements.setExpectedBooks(books);
        expectedBookDtoWithTotalElements.setBookPage(2);
        expectedBookDtoWithTotalElements.setBookCount(10);
        return expectedBookDtoWithTotalElements;
    }
    public ExpectedBook getBookById(UUID id){
        return createBook(1);
    }
    private ExpectedBook createBook(int i){
        ExpectedBook book = new ExpectedBook();
        book.setExpectedBookAuthor("TestExpectedBookAuthor1" + i);
        book.setExpectedBookGenre("TestExpectedBookGenre1" + i);
        book.setExpectedBookLanguage("TestExpectedBookLanguage1" + i);
        book.setExpectedBookImage("" + i);
        book.setExpectedBookItems(4);
        book.setExpectedBookName("TestExpectedBookName1" + i);
        book.setExpectedBookYear(1999);
        book.setExpectedBookId(UUID.randomUUID());
        return book;
    }
}
