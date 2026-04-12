package com.library.FrontendMicroservice.services;

import com.library.FrontendMicroservice.exceptions.UserExeption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownServiceException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final RestTemplate publicRestTemplate;

    public void sendResetCode(String param){
        String url = "http://localhost:8080/api/reset-password/send-code";

        Map<String, String> requestBody = Map.of(
                "param", param

        );
        try {
            publicRestTemplate.postForObject(url, requestBody, Void.class);
        }catch (Exception e){
            throw new UserExeption(e.getMessage());
        }
    }
    public void resetPassword(String param, String code, String newPasssword){
        String url = "http://localhost:8080/api/reset-password/reset";

        Map<String, String> requestBody = Map.of(
                "code", code,
                "param", param,
                "newPassword", newPasssword


        );
        try {
            publicRestTemplate.postForObject(url, requestBody, Void.class);
        }catch (Exception e){
            throw new UserExeption(e.getMessage());
        }
    }


}
