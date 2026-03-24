package com.library.FrontendMicroservice.testclasses;

import com.library.FrontendMicroservice.dto.BookDto;
import com.library.FrontendMicroservice.dto.CategoriesDto;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.CategoryException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BookServiceTest {
    public BookDto getMostPopularBooks(){
        Book book1 = createBook(1);
        Book book2 = createBook(2);
        Book book3 = createBook(3);
        Book book4 = createBook(4);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        BookDto bookDto = new BookDto();
        bookDto.setBooks(books);
        bookDto.setBookPages(2);
        bookDto.setBookCount(10);
        return bookDto;
    }
    public BookDto getRecentlyAddedAt(){
        Book book1 = createBook(5);
        Book book2 = createBook(6);
        Book book3 = createBook(7);
        Book book4 = createBook(8);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        BookDto bookDto = new BookDto();
        bookDto.setBooks(books);
        bookDto.setBookPages(2);
        bookDto.setBookCount(10);
        return bookDto;
    }
    public BookDto getBooks(String sortBy, int page, String genre, String sortDir){
        Book book1 = createBook(9);
        Book book2 = createBook(10);
        Book book3 = createBook(11);
        Book book4 = createBook(12);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        BookDto bookDto = new BookDto();
        bookDto.setBooks(books);
        bookDto.setBookCount(56);
        bookDto.setBookPages(12);
        return bookDto;
    }
    public CategoriesDto getAllCategories(){
        Category category1 = createCategory(5);
        Category category2 = createCategory(6);
        Category category3 = createCategory(7);
        Category category4 = createCategory(8);
        Category category5 = createCategory(9);
        Category category6 = createCategory(10);
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);
        categories.add(category5);
        categories.add(category6);
        CategoriesDto categoriesDto = new CategoriesDto();
        categoriesDto.setCategories(categories);
        return categoriesDto;
    }

    public Book getBookById(UUID id){
        return createBook(1);
    }
    public BookDto getBooksByAuthor(String author){
        Book book1 = createBook(9);
        Book book2 = createBook(10);
        Book book3 = createBook(11);
        Book book4 = createBook(12);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        BookDto bookDto = new BookDto();
        bookDto.setBooks(books);
        bookDto.setBookCount(56);
        bookDto.setBookPages(12);
        return bookDto;
    }
    public BookDto getBooksByGenre(String genre){
        Book book1 = createBook(9);
        Book book2 = createBook(10);
        Book book3 = createBook(11);
        Book book4 = createBook(12);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        BookDto bookDto = new BookDto();
        bookDto.setBooks(books);
        bookDto.setBookCount(56);
        bookDto.setBookPages(12);
        return bookDto;
    }



    private Book createBook(int i){
        Book book = new Book();
        book.setBookAuthor("TestBookAuthor1" + i);
        book.setBookGenre("TestBookGenre1" + i);
        book.setBookLanguage("TestBookLanguage1" + i);
        book.setBookImage("" + i);
        book.setBookItems(4);
        book.setBookPublication("TestBookPublication");
        book.setBookName("TestBookName1" + i);
        book.setBookYear(1999);
        book.setBookId(UUID.randomUUID());
        return book;
    }
    private Category createCategory(int i){
        Category category = new Category();
        category.setName("TestCategory" + i);
        category.setCount(1+i);
        return category;
    }
}
