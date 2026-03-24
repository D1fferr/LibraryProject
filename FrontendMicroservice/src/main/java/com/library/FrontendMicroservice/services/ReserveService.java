package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.exceptions.ResrvationException;
import com.library.FrontendMicroservice.models.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ReserveService {

    private final RestTemplate authorizedRestTemplate;


    public void reserveBook(Reservation reservation){
            try {
                authorizedRestTemplate.postForObject(
                        "http://localhost:8080/api/reservation/auth/create",
                        reservation,
                        HttpStatus.class
                );
            }catch (Exception e){
                throw new ResrvationException(e.getMessage());
        }
    }
}
