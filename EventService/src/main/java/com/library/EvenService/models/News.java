package com.library.EvenService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
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

}
