package com.library.EvenService.dto;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsDTO {

    @Size(min = 1, max = 10000, message = "News must be between 1 and 1000 characters long.")
    private String body;
    @Size(min = 1, max = 100, message = "News name must be between 1 and 100 characters long.")
    private String name;
    private String photo;
}
