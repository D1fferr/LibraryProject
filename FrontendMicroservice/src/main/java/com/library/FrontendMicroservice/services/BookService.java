package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.BookDtoWithTotalElements;
import com.library.FrontendMicroservice.dto.CategoriesDto;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.CategoryException;
import com.library.FrontendMicroservice.models.Book;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service

public class BookService {

    private final RestTemplate publicRestTemplate;

    public BookService(@Qualifier("publicRestTemplate") RestTemplate publicRestTemplate) {
        this.publicRestTemplate = publicRestTemplate;
    }


    public BookDtoWithTotalElements getMostPopularBooks(){
        try {
            BookDtoWithTotalElements books = publicRestTemplate.getForObject(
                    "http://localhost:8080/api/book/most-popular-books",
                    BookDtoWithTotalElements.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDtoWithTotalElements getRecentlyAddedAt(){
        try {
            BookDtoWithTotalElements books = publicRestTemplate.getForObject(
                    "http://localhost:8080/api/book/recently-added-at?booksPerPage=4",
                    BookDtoWithTotalElements.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDtoWithTotalElements getBooks(String sortBy, int page, String genre, String sortDir){
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book")
                    .queryParam("page", page)
                    .queryParam("sortBy", sortBy)
                    .queryParam("genre", genre)
                    .queryParam("sortDir", sortDir);

            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDtoWithTotalElements.class
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
    public BookDtoWithTotalElements getBooksByAuthor(String author){
        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/author/" + author)
                    .queryParam("page", 0);
            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDtoWithTotalElements.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDtoWithTotalElements getBooksByGenre(String genre){
        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/genre/" + genre)
                    .queryParam("page", 0);
            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDtoWithTotalElements.class
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
