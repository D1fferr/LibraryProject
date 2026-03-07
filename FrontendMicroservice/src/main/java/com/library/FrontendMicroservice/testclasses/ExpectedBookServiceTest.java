package com.library.FrontendMicroservice.testclasses;

import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ExpectedBookServiceTest {
    public List<ExpectedBook> getExpectedBooks(){
        ExpectedBook book1 = createBook(1);
        ExpectedBook book2 = createBook(2);
        ExpectedBook book3 = createBook(3);
        ExpectedBook book4 = createBook(4);
        List<ExpectedBook> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        return books;
    }
    private ExpectedBook createBook(int i){
        ExpectedBook book = new ExpectedBook();
        book.setBookAuthor("TestExpectedBookAuthor1" + i);
        book.setBookGenre("TestExpectedBookGenre1" + i);
        book.setBookLanguage("TestExpectedBookLanguage1" + i);
        book.setBookImage("" + i);
        book.setBookItems(4);
        book.setBookName("TestExpectedBookName1" + i);
        book.setBookYear(1999);
        book.setExpectedBookId(UUID.randomUUID());
        return book;
    }
}
