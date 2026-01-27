package com.Library.UserService.services;

import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.util.FailedToConnectWithUserServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Slf4j
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
        try {
            log.info("Trying to connect with user service to send user. ID: '{}'", id);
            restTemplate.postForObject(url, requestBody, Void.class);
            log.info("User sent to user service. ID: '{}'", id);
        }catch (Exception e){
            log.warn("Failed to connect with user service to send user. ID: '{}'", id);
            throw new FailedToConnectWithUserServiceException("Failed to connect with user service to send user");
        }
    }
    public void delete(UUID id){
        String url = "http://localhost:8087/user/delete/" + id.toString();
        try {
            log.info("Trying to connect with user service to delete user. ID: '{}'", id);
            restTemplate.delete(url);
            log.info("User deleted. ID: '{}'", id);
        }catch (Exception e){
            log.warn("Failed to connect with user service to delete user. ID: '{}'", id);
            throw new FailedToConnectWithUserServiceException("Failed to connect with user service to delete user");
        }
    }
    public void sendCredential(AuthUser authUser, UUID id){
        String url = "http://localhost:8087/user/change-credentials/" + id;

        Map<String, String> requestBody = Map.of(
                "username", authUser.getUsername(),
                "email", authUser.getEmail()
        );
        try {
            log.info("Trying to connect with user service to send credentials. ID: '{}'", id);
            restTemplate.patchForObject(url, requestBody, Void.class);
            log.info("Credentials sent to user service. ID: '{}'", id);
        }catch (Exception e){
            log.warn("Failed to connect with user service to send credentials. ID: '{}'", id);
            throw new FailedToConnectWithUserServiceException("Failed to connect with user service to send credentials");
        }
    }
}
