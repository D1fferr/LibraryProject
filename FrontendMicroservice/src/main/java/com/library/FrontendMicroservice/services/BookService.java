package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.CategoryException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final RestTemplate restTemplate;


    public List<Book> getMostPopularBooks(){
        try {
            List<Book> books = restTemplate.getForObject(
                    "http://localhost:8081/book/most-popular-books",
                    List.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public List<Book> getRecentlyAddedAt(){
        try {
            List<Book> books = restTemplate.getForObject(
                    "http://localhost:8081/book/recently-added-at?booksPerPage=4",
                    List.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public List<Book> getBooks(String sortBy, int page, String genre){
        try {
            Object [] params = new Object[]{page, sortBy, genre};
            List<Book> books = restTemplate.getForObject(
                    "http://localhost:8081/book/?page={page}&sortBy={sortBy}&genre={genre}",
                    List.class,
                    params
                    );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public List<Category> getAllCategories(){
        try {
            List<Category> categories = restTemplate.getForObject(
                    "http://localhost:8081/book/category",
                    List.class
            );
            return categories;
        }catch (Exception e){
            throw new CategoryException(e.getMessage());
        }
    }
}
