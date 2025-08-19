package ua.zakharchuk.ExpectedBooksService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "expected_book")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpectedBook {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "expected_book_id")
        private int expectedBookId;

        @Column(name = "expected_book_name")
        @NotEmpty(message = "Book name must not be empty.")
        private String expectedBookName;

        @Column(name = "expected_book_author")
        @NotEmpty(message = "Author name must not be empty.")
        private String expectedBookAuthor;

        @Column(name = "expected_book_year")
        @Min(value = 1000, message = "The year of publication of the book must be greater than 1000.")
        private int expectedBookYear;

        @Column(name = "expected_book_publication")
        @NotEmpty(message = "The Publication must not be empty.")
        private String expectedBookPublication;

        @Column(name = "expected_book_language")
        @NotEmpty(message = "The language of the book cannot be empty.")
        private String expectedBookLanguage;

        @Column(name = "expected_book_pieces")
        private int expectedBookPieces;

        @Column(name = "expected_book_image")
        private String expectedBookImage;

        @Column(name = "expected_book_genre")
        @NotEmpty(message = "Book genre cannot be empty.")
        private String expectedBookGenre;

        @Column(name = "expected_book_added_at", updatable = false)
        private LocalDateTime expectedBookAddedAt;


    }
