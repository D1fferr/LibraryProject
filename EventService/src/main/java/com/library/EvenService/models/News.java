package com.library.EvenService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
public class News {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "news_id")
    private UUID id;
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
