package org.example.firstmvc.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSenderService {
    private final KafkaTemplate<String, String> kafkaTemplate;


    public void send(String id){
        try {
            log.info("Trying to send reservations to kafka. ID: '{}'", id);
            kafkaTemplate.send("reservations", id);
            log.info("Reservations to kafka sent. ID: '{}'", id);
        }catch (Exception e){
            log.info("Failed to send reservations to kafka. Error: '{}'", e.getMessage());
        }

    }
}