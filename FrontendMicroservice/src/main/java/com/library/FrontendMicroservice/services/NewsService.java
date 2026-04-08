package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.AnnouncementDTOForGetRequest;
import com.library.FrontendMicroservice.dto.AnnouncementDTOWithTotalElements;
import com.library.FrontendMicroservice.dto.NewsDTOForGetRequest;
import com.library.FrontendMicroservice.dto.NewsDtoWithTotalElements;
import com.library.FrontendMicroservice.exceptions.AnnouncementException;
import com.library.FrontendMicroservice.exceptions.NewsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final RestTemplate publicRestTemplate;

    public NewsDtoWithTotalElements getAllNews(int page){
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/news/get-all")
                    .queryParam("page", page);

            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    NewsDtoWithTotalElements.class
            );
        }catch (Exception e){
            throw new NewsException(e.getMessage());
        }
    }
    public NewsDTOForGetRequest getNewsById(UUID id){
        try {

            String url = "http://localhost:8080/api/news/get-one/" + id;
            return publicRestTemplate.getForObject(
                    url,
                    NewsDTOForGetRequest.class
            );
        }catch (Exception e){
            throw new AnnouncementException(e.getMessage());
        }
    }

}
