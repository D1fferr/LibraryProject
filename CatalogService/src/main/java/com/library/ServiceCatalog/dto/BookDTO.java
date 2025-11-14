package com.library.ServiceCatalog.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@NoArgsConstructor
public class BookDTO {
    @Size(min = 1, message = "Book name must not be empty.")
    private String bookName;

    @Size(min = 1, message = "Author name must not be empty.")
    private String bookAuthor;

    private int bookYear;

    @Size(min = 1, message = "The Publication must not be empty.")
    private String bookPublication;

    @Size(min = 1, message = "The language of the book cannot be empty.")
    private String bookLanguage;

    private int bookPieces;

    @Size(min = 1, message = "Book genre cannot be empty.")
    private String bookGenre;

}
