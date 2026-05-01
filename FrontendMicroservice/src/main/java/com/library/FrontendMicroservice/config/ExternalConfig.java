package com.library.FrontendMicroservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalConfig {
    private Services services = new Services();


    @Data
    public static class Services {
        private String apiGateway;
    }
}
