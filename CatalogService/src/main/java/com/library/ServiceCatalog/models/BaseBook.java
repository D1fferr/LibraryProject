package com.library.ServiceCatalog.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class BaseBook {
    @Column(name = "book_name")
    @NotEmpty(message = "Book name must not be empty.")
    private String bookName;

    @Column(name = "book_author")
    @NotEmpty(message = "Author name must not be empty.")
    private String bookAuthor;

    @Column(name = "book_year")
    @Min(value = 1000, message = "The year of publication of the book must be greater than 1000.")
    private int bookYear;

    @Column(name = "book_publication")
    @NotEmpty(message = "The Publication must not be empty.")
    private String bookPublication;

    @Column(name = "book_language")
    @NotEmpty(message = "The language of the book cannot be empty.")
    private String bookLanguage;

    @Column(name = "book_pieces")
    private int bookPieces;

    @Column(name = "book_image")
    private String bookImage;

    @Column(name = "book_genre")
    @NotEmpty(message = "Book genre cannot be empty.")
    private String bookGenre;

    @Column(name = "book_added_at", updatable = false)
    private LocalDateTime bookAddedAt;
}
