package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.PageReportAvailabilityErrorDTO;
import com.library.FrontendMicroservice.dto.ReportAvailabilityDTO;
import com.library.FrontendMicroservice.dto.ReportAvailabilityErrorDTOWithId;
import com.library.FrontendMicroservice.exceptions.ExpectedBookException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAvailabilityErrorService {
    private final ExternalConfig config;
    private final RestTemplate authorizedRestTemplate;


    public PageReportAvailabilityErrorDTO getErrors(int page, int pageSize){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/report-availability-error/auth/get-all")
                        .queryParam("page", page)
                        .queryParam("items-per-page", pageSize);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        PageReportAvailabilityErrorDTO.class
                );
        }catch (Exception e){
            log.info("Failed to get errors {}", e.getMessage());
            throw e;
        }
    }

    public void sendNotification(ReportAvailabilityErrorDTOWithId id){
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + "/api/report-availability-error/auth/send";
        try {
            authorizedRestTemplate.postForObject(url, id, String.class);
        }catch(Exception e){
            log.info("Failed to send a notification  {}", e.getMessage());
            throw e;
        }

    }
    public void sendAvailabilityNotification(ReportAvailabilityDTO dto){
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + "/api/report-availability/auth/add";
        try {
            authorizedRestTemplate.postForObject(url, dto, String.class);
        }catch(Exception e){
            log.info("Failed to add a notification  {}", e.getMessage());
            throw e;
        }

    }

}



