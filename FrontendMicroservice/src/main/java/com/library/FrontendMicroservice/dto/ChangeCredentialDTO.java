package com.library.FrontendMicroservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeCredentialDTO {

    private String username;
    private String email;
    private String password;
    private String currentPassword;
    private String libraryCode;

}