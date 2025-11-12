package com.Library.UserService.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCredentialDTO {

    private String username;
    private String email;
    private String password;

}
