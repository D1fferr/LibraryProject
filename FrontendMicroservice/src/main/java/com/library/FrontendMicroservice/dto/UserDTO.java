package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @Size(min = 1,message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be in format email@email.email")
    @Size(min = 1, message = "Email must not be empty")
    private String userEmail;
    private String userLibraryCode;
    @Size(min = 1, message = "Password must not be empty")
    private String userPassword;

}