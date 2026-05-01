package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.exceptions.AnnouncementException;
import com.library.FrontendMicroservice.exceptions.BookException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
public class EventService {
    private final ExternalConfig config;
    private final RestTemplate publicRestTemplate;
    private final RestTemplate authorizedRestTemplate;

    public AnnouncementDTOWithTotalElements getAllEvents(int page){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/announcement/get-all")
                    .queryParam("page", page);

            String url = builder.toUriString();
            return publicRestTemplate.getForObject(
                    url,
                    AnnouncementDTOWithTotalElements.class
            );
        }catch (Exception e){
            log.info("Failed to get all events {}", e.getMessage());
            throw e;
        }

    }
    public AnnouncementDTOWithTotalElements getAllEventsForAdmin(int page, int pageSize, String search){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder;
            if (search!=null){
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/announcement/get-all")
                        .queryParam("page", page)
                        .queryParam("announcementPerPage", pageSize)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        AnnouncementDTOWithTotalElements.class
                );
            }else {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/announcement/get-all")
                        .queryParam("page", page)
                        .queryParam("announcementPerPage", pageSize);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        AnnouncementDTOWithTotalElements.class
                );
            }
        }catch (Exception e){
            log.info("Failed to get all events for admin {}", e.getMessage());
            throw e;
        }

    }

    public void deleteEvent(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + "/api/announcement/auth/delete/" + id.toString();
        try {
            authorizedRestTemplate.delete(url);
        }catch (Exception e){
            log.info("Failed to delete the event {}", e.getMessage());
            throw e;
        }
    }
    public AnnouncementDTOForGetRequest getEventById(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = apiGateway + "/api/announcement/" + id;
            return publicRestTemplate.getForObject(
                    url,
                    AnnouncementDTOForGetRequest.class
            );
        }catch (Exception e){
            log.info("Failed to get an event {}", e.getMessage());
            throw e;
        }
    }
    public void createEvent(AnnouncementDTO dto, MultipartFile coverImage) throws IOException {
        String apiGateway = config.getServices().getApiGateway();
        String bookServiceUrl = apiGateway + "/api/announcement/auth/create";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnnouncementDTO> bookDataEntity = new HttpEntity<>(dto, jsonHeaders);
        body.add("announcementData", bookDataEntity);

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
                AnnouncementDTO.class
        );

    }

    public void updateEvent(UUID id, AnnouncementDTO dto, MultipartFile coverImage) throws IOException {
        String apiGateway = config.getServices().getApiGateway();
        String bookServiceUrl = apiGateway + "/api/announcement/auth/change/" + id;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnnouncementDTO> bookDataEntity = new HttpEntity<>(dto, jsonHeaders);
        body.add("announcementData", bookDataEntity);

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
                String.class
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
