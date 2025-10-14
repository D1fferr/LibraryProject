package com.library.ServiceCatalog.config;

import com.library.ServiceCatalog.dto.BookDTOForKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.library.ServiceCatalog.models.Book;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, BookDTOForKafka> bookConsumerFactory() {
        Map<String, Object> configKafkaConsumer = new HashMap<>();
        configKafkaConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configKafkaConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "catalog-service");
        configKafkaConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configKafkaConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configKafkaConsumer.put(JsonDeserializer.TYPE_MAPPINGS, "book:com.library.ServiceCatalog.dto.BookDTOForKafka");
        configKafkaConsumer.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Book.class);
        return new DefaultKafkaConsumerFactory<>(configKafkaConsumer);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookDTOForKafka> bookConcurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, BookDTOForKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookConsumerFactory());
        return factory;
    }

}
