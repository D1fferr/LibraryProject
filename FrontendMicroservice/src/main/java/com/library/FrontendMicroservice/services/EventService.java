package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.AnnouncementDTOForGetRequest;
import com.library.FrontendMicroservice.dto.AnnouncementDTOWithTotalElements;
import com.library.FrontendMicroservice.dto.BookDtoWithTotalElements;
import com.library.FrontendMicroservice.exceptions.AnnouncementException;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.models.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final RestTemplate publicRestTemplate;

    public AnnouncementDTOWithTotalElements getAllEvents(int page){
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/announcement/get-all")
                    .queryParam("page", page);

            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    AnnouncementDTOWithTotalElements.class
            );
        }catch (Exception e){
            throw new AnnouncementException(e.getMessage());
        }

    }
    public AnnouncementDTOForGetRequest getEventById(UUID id){
        try {

            String url = "http://localhost:8080/api/announcement/" + id;
            return publicRestTemplate.getForObject(
                    url,
                    AnnouncementDTOForGetRequest.class
            );
        }catch (Exception e){
            throw new AnnouncementException(e.getMessage());
        }
    }

}
