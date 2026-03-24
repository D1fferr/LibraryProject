package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.ExpectedBookDto;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class ExpectedBookService {
    private final RestTemplate restTemplate;

    public ExpectedBookService(@Qualifier("publicRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ExpectedBookDto getExpectedBooks(){
        try {
            ExpectedBookDto books = restTemplate.getForObject(
                    "http://localhost:8080/api/expected-book/get-all",
                    ExpectedBookDto.class
            );
            return books;
        }catch (Exception e){
            throw new ExpectedBookException(e.getMessage());
        }
    }
    public ExpectedBook getBookById(UUID id){
        try {

            String url = "http://localhost:8080/api/eexpected-book/" + id;
            return restTemplate.getForObject(
                    url,
                    ExpectedBook.class
            );
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }
}
