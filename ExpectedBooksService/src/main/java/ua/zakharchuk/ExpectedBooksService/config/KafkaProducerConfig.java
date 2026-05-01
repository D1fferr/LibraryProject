package ua.zakharchuk.ExpectedBooksService.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final ExternalConfig config;

    @Bean
    public ProducerFactory<String, ExpectedBookDTOForKafka> expectedBookProducerFactory(){
        String kafkaEndpoint = config.getKafka().getEndpoint();
        Map<String, Object> configKafkaProducer = new HashMap<>();
        configKafkaProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint);
        configKafkaProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configKafkaProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configKafkaProducer.put(JsonSerializer.TYPE_MAPPINGS, "book:ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka");
        return new DefaultKafkaProducerFactory<>(configKafkaProducer);
    }
    @Bean
    public KafkaTemplate<String, ExpectedBookDTOForKafka> expectedBookKafkaTemplate(){
        return new KafkaTemplate<>(expectedBookProducerFactory());
    }


}
