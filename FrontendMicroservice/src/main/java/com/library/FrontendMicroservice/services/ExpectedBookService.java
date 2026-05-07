package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import com.library.FrontendMicroservice.models.ExpectedBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class ExpectedBookService {
    private final ExternalConfig config;
    private final RestTemplate publicRestTemplate;
    private final RestTemplate authorizedRestTemplate;




    public ExpectedBookDtoWithTotalElements getExpectedBooks(){
        String apiGateway = config.getServices().getApiGateway();
        try {
            return publicRestTemplate.getForObject(
                    apiGateway + "/api/expected-book/public/get-all",
                    ExpectedBookDtoWithTotalElements.class
            );
        }catch (Exception e){
            log.info("Failed to get all expected books {}", e.getMessage());
            throw e;
        }
    }
    public ExpectedBook getBookById(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        try {

            String url = apiGateway + "/api/expected-book/public/" + id;
            return publicRestTemplate.getForObject(
                    url,
                    ExpectedBook.class
            );
        }catch (Exception e){
            log.info("Failed to get an expected book {}", e.getMessage());
            throw e;
        }
    }
    public ExpectedBookDtoWithTotalElements getBooksForAdmin(int page, int pageSize, String search){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder;
            if (search!=null){
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/expected-book/public/get-all")
                        .queryParam("page", page)
                        .queryParam("bookPerPage", pageSize)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        ExpectedBookDtoWithTotalElements.class
                );
            }else {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/expected-book/public/get-all")
                        .queryParam("page", page)
                        .queryParam("bookPerPage", pageSize);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        ExpectedBookDtoWithTotalElements.class
                );
            }

        }catch (Exception e){
            log.info("Failed to get all expected books for admin{}", e.getMessage());
            throw e;
        }

    }
    public ExpectedBookDtoWithTotalElements getAllExpectedBooks(int page, String sort, String search){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder;
            if (search!=null){
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/expected-book/public/get-all")
                        .queryParam("page", page)
                        .queryParam("sort", sort)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        ExpectedBookDtoWithTotalElements.class
                );
            }else {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/expected-book/public/get-all")
                        .queryParam("page", page)
                        .queryParam("sort", sort);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        ExpectedBookDtoWithTotalElements.class
                );
            }

        }catch (Exception e){
            log.info("Failed to get all expected books {}", e.getMessage());
            throw e;
        }

    }
    public void addToCurrentBooks(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = apiGateway + "/api/expected-book/auth/add-to-current-books/" + id;
            authorizedRestTemplate.getForObject(
                    url,
                    String.class
            );
        }catch (Exception e){
            log.info("Failed to add an expected book to current books {}", e.getMessage());
            throw e;
        }
    }
    public void deleteBook(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + "/api/expected-book/auth/delete/" + id.toString();
        try {
            authorizedRestTemplate.delete(url);
        }catch (Exception e){
            log.info("Failed to delete the expected book {}", e.getMessage());
            throw e;
        }
    }
    public void createBook(ExpectedBookDTOCreate dto, MultipartFile coverImage) throws IOException {
        String apiGateway = config.getServices().getApiGateway();
        String bookServiceUrl = apiGateway + "/api/expected-book/auth/create";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExpectedBookDTOCreate> bookDataEntity = new HttpEntity<>(dto, jsonHeaders);
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

    public void updateBook(UUID id, ExpectedBookDTOCreate dto, MultipartFile coverImage) throws IOException {
        String apiGateway = config.getServices().getApiGateway();
        String bookServiceUrl = apiGateway+ "/api/expected-book/auth/change/" + id;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExpectedBookDTOCreate> bookDataEntity = new HttpEntity<>(dto, jsonHeaders);
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
