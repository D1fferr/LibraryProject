package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportAvailabilityErrorDTO {

    private UUID userId;
    private UUID expectedBookId;
    private String userEmail;
    private Status status;
    private String username;
    private String error;

}