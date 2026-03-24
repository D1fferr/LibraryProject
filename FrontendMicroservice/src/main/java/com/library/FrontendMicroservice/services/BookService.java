package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.BookDto;
import com.library.FrontendMicroservice.dto.CategoriesDto;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.CategoryException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Service

public class BookService {

    private final RestTemplate publicRestTemplate;

    public BookService(@Qualifier("publicRestTemplate") RestTemplate publicRestTemplate) {
        this.publicRestTemplate = publicRestTemplate;
    }


    public BookDto getMostPopularBooks(){
        try {
            BookDto books = publicRestTemplate.getForObject(
                    "http://localhost:8080/api/book/most-popular-books",
                    BookDto.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDto getRecentlyAddedAt(){
        try {
            BookDto books = publicRestTemplate.getForObject(
                    "http://localhost:8080/api/book/recently-added-at?booksPerPage=4",
                    BookDto.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDto getBooks(String sortBy, int page, String genre, String sortDir){
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book")
                    .queryParam("page", page)
                    .queryParam("sortBy", sortBy)
                    .queryParam("genre", genre)
                    .queryParam("sortDir", sortDir);

            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDto.class
                    );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public Book getBookById(UUID id){
        try {

            String url = "http://localhost:8080/api/book/" + id;
            return publicRestTemplate.getForObject(
                    url,
                    Book.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDto getBooksByAuthor(String author){
        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/" + author)
                    .queryParam("page", 0);
            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDto.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDto getBooksByGenre(String genre){
        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/" + genre)
                    .queryParam("page", 0);
            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDto.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }




    public CategoriesDto getAllCategories(){
        try {
            CategoriesDto categories = publicRestTemplate.getForObject(
                    "http://localhost:8080/api/book/category",
                    CategoriesDto.class
            );
            return categories;
        }catch (Exception e){
            throw new CategoryException(e.getMessage());
        }
    }
}
