package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class NewsDTOForGetRequest {
    private UUID id;
    @Size(min = 1, max = 10000, message = "News must be between 1 and 1000 characters long.")
    private String body;
    @Size(min = 1, max = 100, message = "News name must be between 1 and 100 characters long.")
    private String name;
}
