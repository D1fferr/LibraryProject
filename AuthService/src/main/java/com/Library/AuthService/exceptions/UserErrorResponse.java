package com.Library.AuthService.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class UserErrorResponse {
    private String message;

}
