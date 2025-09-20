package com.library.EvenService.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AnnouncementDTO {
    @Size(min = 1, max = 1000, message = "The announcement must be between 1 and 1000 characters long.")
    private String body;
    @Size(min = 1, max = 100, message = "Announcement name must be between 1 and 100 characters long. ")
    private String name;
    @Size(min = 1, message = "Announcement type must not be empty.")
    private String type;
    @FutureOrPresent(message = "The announcement date cannot be in the past.")
    private LocalDate date;
    private String photo;
    private Boolean addEvent;

    public AnnouncementDTO(){}

    public AnnouncementDTO(String body, String name, String type, LocalDate date, String photo, Boolean addEvent) {
        this.body = body;
        this.name = name;
        this.type = type;
        this.date = date;
        this.photo = photo;
        this.addEvent = addEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getAddEvent() {
        return addEvent;
    }

    public void setAddEvent(Boolean addEvent) {
        this.addEvent = addEvent;
    }
}
