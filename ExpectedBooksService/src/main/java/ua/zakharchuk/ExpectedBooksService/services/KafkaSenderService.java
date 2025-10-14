package ua.zakharchuk.ExpectedBooksService.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOForKafka;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaSenderService {

    private final KafkaTemplate<String, ExpectedBookDTOForKafka> expectedBookKafkaTemplate;
    private final ExpectedBookService expectedBookService;
    private final EmailSenderService emailSenderService;
    public void send(UUID id){
        ExpectedBookDTOForKafka expectedBookDTOForKafka = expectedBookService.findOneBookForKafka(id);
        expectedBookKafkaTemplate.send("expected-book-topic", expectedBookDTOForKafka);
        emailSenderService.send(id);
    }
}
