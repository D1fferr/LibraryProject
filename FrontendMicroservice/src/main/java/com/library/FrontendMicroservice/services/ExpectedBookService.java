package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.ExpectedBookDtoWithTotalElements;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import com.library.FrontendMicroservice.models.ExpectedBook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class ExpectedBookService {
    private final RestTemplate restTemplate;

    public ExpectedBookService(@Qualifier("publicRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ExpectedBookDtoWithTotalElements getExpectedBooks(){
        try {
            ExpectedBookDtoWithTotalElements books = restTemplate.getForObject(
                    "http://localhost:8080/api/expected-book/get-all",
                    ExpectedBookDtoWithTotalElements.class
            );
            return books;
        }catch (Exception e){
            throw new ExpectedBookException(e.getMessage());
        }
    }
    public ExpectedBook getBookById(UUID id){
        try {

            String url = "http://localhost:8080/api/expected-book/" + id;
            return restTemplate.getForObject(
                    url,
                    ExpectedBook.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
}
