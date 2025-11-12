package com.Library.UserService.services;

import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrossServerRequestService {

    private final RestTemplate restTemplate;

    public void sendUser(UserDTO userDTO, UUID id){
        String url = "http://localhost:8087/user/create";

        Map<String, String> requestBody = Map.of(
                "id", id.toString(),
                "username", userDTO.getUsername(),
                "email", userDTO.getUserEmail(),
                "libraryCode", userDTO.getUserLibraryCode()
        );
        restTemplate.postForObject(url, requestBody, Void.class);
    }
    public void delete(UUID id){
        String url = "http://localhost:8087/user/delete/" + id.toString();
        restTemplate.delete(url);
    }
    public void sendCredential(AuthUser authUser, UUID id){
        String url = "http://localhost:8087/user/change-credentials/" + id;

        Map<String, String> requestBody = Map.of(
                "username", authUser.getUsername(),
                "email", authUser.getEmail()
        );
        restTemplate.patchForObject(url, requestBody, Void.class);
    }
}
