package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSenderService {

    private final KafkaTemplate<String, ExpectedBookDTOForKafka> expectedBookKafkaTemplate;
    private final ExpectedBookService expectedBookService;
    private final EmailSenderService emailSenderService;

    public void send(UUID id){
        ExpectedBookDTOForKafka expectedBookDTOForKafka = expectedBookService.findOneBookForKafka(id);
        try {
            log.info("Trying to sent the expected book to kafka. ID: '{}'", id);
            expectedBookKafkaTemplate.send("expected-book-topic", expectedBookDTOForKafka);
        }catch (Exception e){
            log.info("Failed to sent the expected book to kafka. ID: '{}', Errors: '{}'", id, e.getMessage());
        }

        emailSenderService.send(id);
    }
}
