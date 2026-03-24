package com.library.FrontendMicroservice.config;

import com.library.FrontendMicroservice.auth.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean(name = "authorizedRestTemplate")
    public RestTemplate authorizedRestTemplate(JwtInterceptor jwtInterceptor){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(jwtInterceptor));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }
    @Bean(name = "publicRestTemplate")
    public RestTemplate publicRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }
}
