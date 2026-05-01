package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.CategoryException;
import com.library.FrontendMicroservice.exceptions.ReservationException;
import com.library.FrontendMicroservice.exceptions.UserExeption;
import com.library.FrontendMicroservice.models.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final ExternalConfig config;
    private final RestTemplate publicRestTemplate;
    private final RestTemplate authorizedRestTemplate;




    public BookDtoWithTotalElements getMostPopularBooks(){
        String apiGateway = config.getServices().getApiGateway();
        try {
            BookDtoWithTotalElements books = publicRestTemplate.getForObject(
                    apiGateway + "/api/book/public/most-popular-books",
                    BookDtoWithTotalElements.class
            );
            return books;
        }catch (Exception e){
            log.info("Failed to get most popular books {}", e.getMessage());
            throw e;
        }
    }
    public BookDtoWithTotalElements getRecentlyAddedAt(){
        String apiGateway = config.getServices().getApiGateway();
        try {
            BookDtoWithTotalElements books = publicRestTemplate.getForObject(
                    apiGateway + "/api/book/public/recently-added-at?booksPerPage=4",
                    BookDtoWithTotalElements.class
            );
            return books;
        }catch (Exception e){
            log.info("Failed to get recently added books {}", e.getMessage());
            throw e;
        }
    }
    public BookDtoWithTotalElements getBooks(String search, String sortBy, int page, String genre, String sortDir){
        String apiGateway = config.getServices().getApiGateway();
        try {
            if (search!=null){
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/book/public")
                    .queryParam("page", page)
                    .queryParam("sortBy", sortBy)
                    .queryParam("search", search)
                    .queryParam("genre", genre)
                    .queryParam("sortDir", sortDir);

            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDtoWithTotalElements.class
                    );
            }else {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/book/public")
                        .queryParam("page", page)
                        .queryParam("sortBy", sortBy)
                        .queryParam("genre", genre)
                        .queryParam("sortDir", sortDir);

                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        BookDtoWithTotalElements.class
                );
            }
        }catch (Exception e){
            log.info("Failed to get all books {}", e.getMessage());
            throw e;
        }
    }
    public BookDtoWithTotalElements getBooksForAdmin(int page, String search){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder;
            if (search!=null){
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/book/auth")
                        .queryParam("page", page)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        BookDtoWithTotalElements.class
                );
            }else {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/book/auth")
                        .queryParam("page", page);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        BookDtoWithTotalElements.class
                );
            }

        }catch (Exception e){
            log.info("Failed to get all books for admin {}", e.getMessage());
            throw e;
        }

    }

    public void deleteBook(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + "/api/book/auth/delete/" + id.toString();
        try {
            authorizedRestTemplate.delete(url);
        }catch (Exception e){
            log.info("Failed to delete the book {}", e.getMessage());
            throw e;
        }
    }
    public Book getBookById(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = apiGateway + "/api/book/public/" + id;
            return publicRestTemplate.getForObject(
                    url,
                    Book.class
            );
        }catch (Exception e){
            log.info("failed to get a book {}", e.getMessage());
            throw e;
        }
    }
    public BookDtoWithTotalElements getBooksByAuthor(String author){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = apiGateway + "/api/book/public/author/{author}";
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("page", 0)
                    .buildAndExpand(author);
            return publicRestTemplate.getForObject(
                    builder.toUri(),
                    BookDtoWithTotalElements.class
            );
        }catch (Exception e){
            log.info("Failed to get books by author{}", e.getMessage());
            throw e;
        }
    }
    public BookDtoWithTotalElements getBooksByGenre(String genre){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/book/public/genre/{genre}")
                    .queryParam("page", 0)
                    .encode()
                    .buildAndExpand(genre)
                    .toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    BookDtoWithTotalElements.class
            );
        }catch (Exception e){
            log.info("Failed to get books by genre{}", e.getMessage());
            throw e;
        }
    }
    public CategoriesDto getAllCategories(){
        String apiGateway = config.getServices().getApiGateway();
        try {
            CategoriesDto categories = publicRestTemplate.getForObject(
                    apiGateway + "/api/book/public/category",
                    CategoriesDto.class
            );
            return categories;
        }catch (Exception e){
            log.info("Failed to get all categories{}", e.getMessage());
            throw e;
        }
    }
    public BookDtoWithTotalElements getBooksByIds(BookDtoForReservations dto, int page){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/book/auth/for-reservations")
                    .queryParam("page", page).toUriString();
            return authorizedRestTemplate.postForObject(
                    url,
                    dto,
                    BookDtoWithTotalElements.class
            );
        }catch (Exception e){
            log.info("Failed to get books by IDs {}", e.getMessage());
            throw e;
        }

    }
    public void createBook(BookDTOForCreate dto, MultipartFile coverImage) throws IOException {
        String apiGateway = config.getServices().getApiGateway();
        String bookServiceUrl = apiGateway + "/api/book/auth/create";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookDTOForCreate> bookDataEntity = new HttpEntity<>(dto, jsonHeaders);
        body.add("bookData", bookDataEntity);

        if (coverImage != null && !coverImage.isEmpty()) {
            ByteArrayResource fileResource = new ByteArrayResource(coverImage.getBytes()) {
                @Override
                public String getFilename() {
                    return coverImage.getOriginalFilename();
                }
            };

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(getContentType(coverImage));

            HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileResource, fileHeaders);
            body.add("coverImage", fileEntity);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        authorizedRestTemplate.exchange(
                bookServiceUrl,
                HttpMethod.POST,
                requestEntity,
                HttpStatus.class
        );

    }

    public void updateBook(UUID id, BookDto dto, MultipartFile coverImage) throws IOException {
        String apiGateway = config.getServices().getApiGateway();
        String bookServiceUrl = apiGateway + "/api/book/auth/change-book/" + id;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookDto> bookDataEntity = new HttpEntity<>(dto, jsonHeaders);
        body.add("bookData", bookDataEntity);

        if (coverImage != null && !coverImage.isEmpty()) {
            ByteArrayResource fileResource = new ByteArrayResource(coverImage.getBytes()) {
                @Override
                public String getFilename() {
                    return coverImage.getOriginalFilename();
                }
            };

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(getContentType(coverImage));

            HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileResource, fileHeaders);
            body.add("coverImage", fileEntity);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        System.out.println("Trying to connect");
        authorizedRestTemplate.exchange(
                bookServiceUrl,
                HttpMethod.PATCH,
                requestEntity,
                Void.class
        );

    }

    private MediaType getContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType != null) {
            return MediaType.parseMediaType(contentType);
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            if (filename.toLowerCase().endsWith(".png")) {
                return MediaType.IMAGE_PNG;
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                return MediaType.IMAGE_JPEG;
            } else if (filename.toLowerCase().endsWith(".webp")) {
                return MediaType.parseMediaType("image/webp");
            }
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
