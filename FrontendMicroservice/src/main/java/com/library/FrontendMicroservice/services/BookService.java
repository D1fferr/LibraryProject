package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.BookDtoForReservations;
import com.library.FrontendMicroservice.dto.BookDtoWithTotalElements;
import com.library.FrontendMicroservice.dto.CategoriesDto;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.CategoryException;
import com.library.FrontendMicroservice.exceptions.ReservationException;
import com.library.FrontendMicroservice.models.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final RestTemplate publicRestTemplate;
    private final RestTemplate authorizedRestTemplate;




    public BookDtoWithTotalElements getMostPopularBooks(){
        try {
            BookDtoWithTotalElements books = publicRestTemplate.getForObject(
                    "http://localhost:8080/api/book/public/most-popular-books",
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
                    "http://localhost:8080/api/book/public/recently-added-at?booksPerPage=4",
                    BookDtoWithTotalElements.class
            );
            return books;
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
    public BookDtoWithTotalElements getBooks(String sortBy, int page, String genre, String sortDir){
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/public")
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

            String url = "http://localhost:8080/api/book/public/" + id;
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

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/public/author/" + author)
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

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/book/public/genre/" + genre)
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
                    "http://localhost:8080/api/book/public/category",
                    CategoriesDto.class
            );
            return categories;
        }catch (Exception e){
            throw new CategoryException(e.getMessage());
        }
    }
    public BookDtoWithTotalElements getBooksByIds(BookDtoForReservations dto){

        try {
            return authorizedRestTemplate.postForObject(
                    "http://localhost:8080/api/book/auth/for-reservations",
                    dto,
                    BookDtoWithTotalElements.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }

    }
}
