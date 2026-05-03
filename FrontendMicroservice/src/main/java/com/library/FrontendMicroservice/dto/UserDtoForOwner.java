package com.library.FrontendMicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDtoForOwner {
    private UUID id;
    private String username;
    private String email;
    private String libraryCode;
    private String role;
}
