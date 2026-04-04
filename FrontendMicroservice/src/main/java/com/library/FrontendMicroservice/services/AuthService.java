package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.dto.UserDTO;
import com.library.FrontendMicroservice.exceptions.AuthRequestException;
import com.library.FrontendMicroservice.models.AuthRequest;
import com.library.FrontendMicroservice.models.RegisterRequest;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthService {

    private final RestTemplate publicRestTemplate;

    private final String authServiceUrl = "http://localhost:8080/api/auth";

    public AuthService(@Qualifier("publicRestTemplate") RestTemplate publicRestTemplate) {
        this.publicRestTemplate = publicRestTemplate;
    }

    public String login(AuthRequest authRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("start");

        HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);
        ResponseEntity<AuthRequest> response = publicRestTemplate.exchange(
                authServiceUrl + "/login",
                HttpMethod.POST,
                entity,
                AuthRequest.class
        );
        System.out.println("finish");
        return extractTokenFromHeaders(response.getHeaders());

    }
    public String register(RegisterRequest registerRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO userDTO = convertToUserDTO(registerRequest);
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
        ResponseEntity<HttpStatus> response = publicRestTemplate.exchange(
                authServiceUrl + "/registration",
                HttpMethod.POST,
                entity,
                HttpStatus.class
        );

        return extractTokenFromHeaders(response.getHeaders());

    }



    private String extractTokenFromHeaders(HttpHeaders headers) {
        String authHeader = headers.getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    private UserDTO convertToUserDTO(RegisterRequest registerRequest){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(registerRequest.getUsername());
        userDTO.setUserEmail(registerRequest.getEmail());
        userDTO.setUserPassword(registerRequest.getPassword());
        if (registerRequest.getLibraryCode()!=null)
            userDTO.setUserLibraryCode(registerRequest.getLibraryCode());
        return userDTO;
    }

}
