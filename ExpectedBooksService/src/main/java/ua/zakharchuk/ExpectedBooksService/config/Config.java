package ua.zakharchuk.ExpectedBooksService.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;

import java.util.Properties;

@Configuration
public class Config {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

//    @Bean
//    public KafkaProducer<String, ExpectedBookDTO> kafkaProducer(){
//        Properties properties = new Properties();
//        properties.put()
//        return new KafkaProducer<String, ExpectedBookDTO>(properties);
//    }
}
