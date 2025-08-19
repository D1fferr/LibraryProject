package ua.zakharchuk.ExpectedBooksService.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ExpectedBookErrorResponse {
    private String message;
}
