package com.library.orderservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalConfig {

    private Kafka kafka = new Kafka();
    private Mail mail = new Mail();
    private Services services = new Services();

    @Data
    public static class Kafka {
        private String endpoint;
    }
    @Data
    public static class Mail {
        private String login;
        private String password;
        private String from;
        private String host;
    }

    @Data
    public static class Services {
        private String user;
        private String catalog;
    }
}