package com.library.EvenService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "announcement")
@Data
@NoArgsConstructor
public class Announcement {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
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
    @Column(name = "announcement_created_date")
    private LocalDate createdDate;

}
