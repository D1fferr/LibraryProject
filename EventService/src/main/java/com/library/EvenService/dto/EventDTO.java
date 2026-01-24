package com.library.EvenService.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class EventDTO {
    private String name;
    private String type;
    private LocalDate date;

}
