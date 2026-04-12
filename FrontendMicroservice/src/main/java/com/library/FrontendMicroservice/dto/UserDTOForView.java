package com.library.FrontendMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTOForView {
    private String username;
    private String email;
    private String libraryCode;
}
