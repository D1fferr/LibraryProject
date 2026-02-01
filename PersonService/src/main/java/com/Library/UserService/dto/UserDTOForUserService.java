package com.Library.UserService.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class UserDTOForUserService {

    private UUID userId;
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be in format email@email.email")
    @NotEmpty(message = "Email must not be empty")
    private String userEmail;
    private String userLibraryCode;
}
