package com.library.EvenService.dto;


import java.time.LocalDate;

public class EventDTO {
    private String name;
    private String type;
    private LocalDate date;


    public EventDTO(){}

    public EventDTO(String name, String type, LocalDate date) {
        this.name = name;
        this.type = type;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
