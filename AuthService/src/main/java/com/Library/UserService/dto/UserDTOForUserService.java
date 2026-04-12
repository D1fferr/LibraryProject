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
    private String username;
    private String userEmail;
    private String userLibraryCode;
}
