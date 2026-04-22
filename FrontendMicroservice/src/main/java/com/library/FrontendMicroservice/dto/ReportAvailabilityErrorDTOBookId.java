package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportAvailabilityErrorDTOBookId {
    @NotNull(message = "Field book id must not be empty")
    private UUID expectedBookId;
}
