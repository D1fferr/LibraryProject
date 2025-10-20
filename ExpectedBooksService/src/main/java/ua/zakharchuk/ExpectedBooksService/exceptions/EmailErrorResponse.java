package ua.zakharchuk.ExpectedBooksService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EmailErrorResponse {
    private String message;
}
