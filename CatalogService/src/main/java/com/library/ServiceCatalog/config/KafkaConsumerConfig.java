package com.library.ServiceCatalog.config;

import com.library.ServiceCatalog.dto.BookDTOForKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ExternalConfig config;




    @Bean
    public ConsumerFactory<String, BookDTOForKafka> bookConsumerFactory() {
        String kafkaEndpoint = config.getKafka().getEndpoint();
        Map<String, Object> configKafkaConsumer = new HashMap<>();
        configKafkaConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        configKafkaConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "catalog-service");
        configKafkaConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configKafkaConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configKafkaConsumer.put(JsonDeserializer.TYPE_MAPPINGS, "book:com.library.ServiceCatalog.dto.BookDTOForKafka");
        configKafkaConsumer.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BookDTOForKafka.class);
        configKafkaConsumer.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(configKafkaConsumer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookDTOForKafka> bookConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookDTOForKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> reservationConsumerFactory() {
        String kafkaEndpoint = config.getKafka().getEndpoint();
        Map<String, Object> configKafkaConsumer = new HashMap<>();
        configKafkaConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        configKafkaConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "catalog-service");
        configKafkaConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configKafkaConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configKafkaConsumer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> reservationConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(reservationConsumerFactory());
        return factory;
    }

}
