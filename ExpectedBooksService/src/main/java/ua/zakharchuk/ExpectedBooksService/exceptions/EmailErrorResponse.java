package ua.zakharchuk.ExpectedBooksService.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EmailErrorResponse {
    String message;
}
