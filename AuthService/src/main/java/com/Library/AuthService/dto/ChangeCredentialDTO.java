package com.Library.AuthService.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCredentialDTO {

    private String username;
    private String email;
    private String password;
    private String currentPassword;
    private String libraryCode;

}
