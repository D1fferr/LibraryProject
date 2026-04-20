package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BookDto {

    private UUID bookId;
    @Size(min = 1, message = "Book name must not be empty.")
    private String bookName;

    @Size(min = 1, message = "Author name must not be empty.")
    private String bookAuthor;

    private int bookYear;

    @Size(min = 1, message = "The Publication must not be empty.")
    private String bookPublication;

    @Size(min = 1, message = "The language of the book cannot be empty.")
    private String bookLanguage;

    private int bookItems;

    @Size(min = 1, message = "Book genre cannot be empty.")
    private String bookGenre;
}