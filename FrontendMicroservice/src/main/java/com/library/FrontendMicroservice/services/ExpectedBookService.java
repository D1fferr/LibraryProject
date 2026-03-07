package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.models.ExpectedBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ExpectedBookService {
    private final RestTemplate restTemplate;


    public List<ExpectedBook> getExpectedBooks(){
        try {
            List<ExpectedBook> books = restTemplate.getForObject(
                    "http://localhost:8082/expected-book/get-all",
                    List.class
            );
            return books;
        }catch (Exception e){
            throw new ExpectedBookException(e.getMessage());
        }
    }
}
