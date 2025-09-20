package com.library.EvenService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private int id;
    @NotNull(message = "News should not be empty")
    @Column(name = "news_body")
    private String body;
    @Column(name = "news_name")
    @NotNull(message = "News name must not be empty")
    @Size(min = 1, max = 100, message = "News name must be between 1 and 100 characters long.")
    private String name;
    @Column(name = "news_date")
    private LocalDate date;
    @Column(name = "news_photo")
    private String photo;

    public News(){}

    public News(String body, String name, LocalDate date, String photo) {
        this.body = body;
        this.name = name;
        this.date = date;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
}
