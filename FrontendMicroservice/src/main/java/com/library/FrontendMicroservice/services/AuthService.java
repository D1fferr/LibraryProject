package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.config.ExternalConfig;
import com.library.FrontendMicroservice.dto.UserDTO;
import com.library.FrontendMicroservice.exceptions.AuthRequestException;
import com.library.FrontendMicroservice.models.AuthRequest;
import com.library.FrontendMicroservice.models.RegisterRequest;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {
    private final ExternalConfig config;
    private final RestTemplate publicRestTemplate;
    private final RestTemplate authorizedRestTemplate;

    private final String authServiceUrl = "/api/auth";

    public String login(AuthRequest authRequest) {
        String apiGateway = config.getServices().getApiGateway();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);
        ResponseEntity<AuthRequest> response = publicRestTemplate.exchange(
                apiGateway + authServiceUrl + "/public/login",
                HttpMethod.POST,
                entity,
                AuthRequest.class
        );
        return extractTokenFromHeaders(response.getHeaders());

    }
    public String register(RegisterRequest registerRequest) {
        String apiGateway = config.getServices().getApiGateway();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO userDTO = convertToUserDTO(registerRequest);
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
        ResponseEntity<HttpStatus> response = publicRestTemplate.exchange(
                apiGateway + authServiceUrl + "/public/registration",
                HttpMethod.POST,
                entity,
                HttpStatus.class
        );

        return extractTokenFromHeaders(response.getHeaders());

    }
    public void logout(){
        String apiGateway = config.getServices().getApiGateway();
        String url = apiGateway + authServiceUrl + "/auth/logout";
            try {
                authorizedRestTemplate.postForObject(url, "Logout", String.class);
                log.info("Logout successful");
            }catch(Exception e){
                log.info("Failed to logout {}", e.getMessage());
                throw e;
            }

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
