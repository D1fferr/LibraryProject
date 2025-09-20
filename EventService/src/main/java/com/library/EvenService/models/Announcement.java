package com.library.EvenService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private UUID id;
    @NotEmpty(message = "Announcement name must not be empty.")
    @Size(min = 1, max = 100, message = "Announcement name must be between 1 and 100 characters long.")
    @Column(name = "announcement_name")
    private String name;
    @Column(name = "announcement_body")
    @NotEmpty(message = "The announcement should not be empty.")
    private String body;
    @NotEmpty(message = "Announcement type must not be empty.")
    @Column(name = "announcement_type")
    private String type;
    @FutureOrPresent(message = "The announcement date cannot be in the past.")
    @Column(name = "announcement_date")
    private LocalDate date;
    @Column(name = "announcement_photo")
    private String photo;
    @Column(name = "add_event")
    private Boolean addEvent;
    @Column(name = "announcement_created_date")
    private LocalDate createdDate;

    public Announcement(){}

    public Announcement(String body, String name, String type, LocalDate date, String photo, Boolean addEvent, LocalDate createdDate) {
        this.body = body;
        this.name = name;
        this.type = type;
        this.date = date;
        this.photo = photo;
        this.addEvent = addEvent;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
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
