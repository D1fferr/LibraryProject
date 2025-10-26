package com.Library.UserService.dto;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    @Size(min = 1, message = "Username must not be empty")
    private String username;
    @Size(min = 1, message = "Password must not be empty")
    private String password;
    @Size(min = 1, message = "Field user role must not be empty")
    private String role;

}
