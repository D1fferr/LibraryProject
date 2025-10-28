package library.com.userservice1.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
@Setter
public class UserErrorResponse {
    private String message;
}
