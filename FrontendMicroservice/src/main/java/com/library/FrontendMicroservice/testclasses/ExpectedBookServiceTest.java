package com.library.FrontendMicroservice.testclasses;

import com.library.FrontendMicroservice.dto.ExpectedBookDto;
import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ExpectedBookServiceTest {
    public ExpectedBookDto getExpectedBooks(){
        ExpectedBook book1 = createBook(1);
        ExpectedBook book2 = createBook(2);
        ExpectedBook book3 = createBook(3);
        ExpectedBook book4 = createBook(4);
        List<ExpectedBook> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        ExpectedBookDto expectedBookDto = new ExpectedBookDto();
        expectedBookDto.setExpectedBooks(books);
        expectedBookDto.setBookPage(2);
        expectedBookDto.setBookCount(10);
        return expectedBookDto;
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
