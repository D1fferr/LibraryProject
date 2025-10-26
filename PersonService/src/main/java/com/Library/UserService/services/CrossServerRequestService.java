package com.Library.UserService.services;

import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.dto.UserDTOForUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrossServerRequestService {

    private final RestTemplate restTemplate;

    public void send(UserDTO userDTO, UUID id){
        String url = "http://localhost:8085/user/create";

        Map<String, String> requestBody = Map.of(
                "id", id.toString(),
                "username", userDTO.getUsername(),
                "email", userDTO.getUserEmail(),
                "libraryCode", userDTO.getUserLibraryCode()
        );
        restTemplate.postForObject(url, requestBody, Void.class);
    }
}
