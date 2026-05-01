package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.ReservationException;
import com.library.FrontendMicroservice.exceptions.UserExeption;
import com.library.FrontendMicroservice.models.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate authorizedRestTemplate;
    private final ExternalConfig config;
    public UserDTOForView getUserById(UUID id){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = apiGateway + "/api/user/" + id;
            return authorizedRestTemplate.getForObject(
                    url,
                    UserDTOForView.class
            );
        }catch (Exception e){
            log.info("Failed to get an user {}", e.getMessage());
            throw e;
        }
    }
    public void updateCredentials(String id, ChangeCredentialDTO dto){
        String apiGateway = config.getServices().getApiGateway();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ChangeCredentialDTO> entity = new HttpEntity<>(dto, headers);
            String authServiceUrl = apiGateway + "/api/auth";
            System.out.println("trying make a request to api");
            authorizedRestTemplate.exchange(
                    authServiceUrl + "/change-credentials/" + id,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        }catch (Exception e){
            log.info("Failed to update credentials {}", e.getMessage());
            throw e;
        }
    }

    public UserDtoWithListUsers getUsersById(UserDtoForReservations dto, int page){
        String apiGateway = config.getServices().getApiGateway();
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/user/for-reservations")
                    .queryParam("page", page).toUriString();
            return authorizedRestTemplate.postForObject(
                    url,
                    dto,
                    UserDtoWithListUsers.class
            );
        }catch (Exception e){
            log.info("Failed to get users by IDs {}", e.getMessage());
            throw e;
        }

    }
    public UserPageDto getAllUsers(int page, int pageSize, String search){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder;
            if (search!=null){
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/user/get-all")
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize)
                        .queryParam("search", search);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        UserPageDto.class
                );
            }else {
                builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/user/get-all")
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize);
                String url = builder.toUriString();
                return authorizedRestTemplate.getForObject(
                        url,
                        UserPageDto.class
                );
            }

        }catch (Exception e){
            log.info("Failed to get all users {}", e.getMessage());
            throw e;
        }


    }

    public void updateLibraryCode(UUID id, UserDTOForChangeProfile dto){
        String apiGateway = config.getServices().getApiGateway();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiGateway + "/api/user/change-profile/" + id.toString());

            String url = builder.toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserDTOForChangeProfile> entity = new HttpEntity<>(dto, headers);
            authorizedRestTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );
        }catch (Exception e){
            log.info("Failed to update a library code {}", e.getMessage());
            throw e;
        }
    }
}
