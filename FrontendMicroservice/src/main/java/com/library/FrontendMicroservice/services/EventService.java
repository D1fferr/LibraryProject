package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.exceptions.AnnouncementException;
import com.library.FrontendMicroservice.exceptions.BookException;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class EventService {
    private final RestTemplate publicRestTemplate;
    private final RestTemplate authorizedRestTemplate;

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
    public AnnouncementDTOWithTotalElements getAllEventsForAdmin(int page, int pageSize, String search){
        try {
            UriComponentsBuilder builder;
            if (search!=null){
                builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/announcement/get-all")
                        .queryParam("page", page)
                        .queryParam("announcementPerPage", pageSize)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        AnnouncementDTOWithTotalElements.class
                );
            }else {
                builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/announcement/get-all")
                        .queryParam("page", page)
                        .queryParam("announcementPerPage", pageSize);
                String url = builder.toUriString();
                return publicRestTemplate.getForObject(
                        url,
                        AnnouncementDTOWithTotalElements.class
                );
            }
        }catch (Exception e){
            throw new AnnouncementException(e.getMessage());
        }

    }

    public void deleteEvent(UUID id){
        String url = "http://localhost:8080/api/announcement/auth/delete/" + id.toString();
        try {
            authorizedRestTemplate.delete(url);
        }catch (Exception e){
            throw new BookException(e.getMessage());
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
    public void createEvent(AnnouncementDTO dto, MultipartFile coverImage) throws IOException {
        final String bookServiceUrl = "http://localhost:8080/api/announcement/auth/create";

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
        final String bookServiceUrl = "http://localhost:8080/api/announcement/auth/change/" + id;

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
