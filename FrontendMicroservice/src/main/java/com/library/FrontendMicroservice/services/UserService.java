package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.ChangeCredentialDTO;
import com.library.FrontendMicroservice.dto.UserDTOForView;
import com.library.FrontendMicroservice.exceptions.BookException;
import com.library.FrontendMicroservice.exceptions.UserExeption;
import com.library.FrontendMicroservice.models.AuthRequest;
import com.library.FrontendMicroservice.models.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate authorizedRestTemplate;

    public UserDTOForView getUserById(UUID id){
        try {

            String url = "http://localhost:8080/api/user/" + id;
            return authorizedRestTemplate.getForObject(
                    url,
                    UserDTOForView.class
            );
        }catch (Exception e){
            throw new UserExeption(e.getMessage());
        }
    }
    public void updateCredentials(String id, ChangeCredentialDTO dto){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ChangeCredentialDTO> entity = new HttpEntity<>(dto, headers);
            String authServiceUrl = "http://localhost:8080/api/auth";
            System.out.println("trying make a request to api");
            authorizedRestTemplate.exchange(
                    authServiceUrl + "/change-credentials/" + id,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new UserExeption(e.getMessage());
        }
    }

}
