package com.library.FrontendMicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTOForCreate {
    @Size(min = 1, message = "Book name must not be empty.")
    private String bookName;

    @Size(min = 1, message = "Author name must not be empty.")
    private String bookAuthor;

    @Min(value = 1000, message = "Year must be valid")
    private Integer bookYear;

    @Size(min = 1, message = "The Publication must not be empty.")
    private String bookPublication;

    @Size(min = 1, message = "The language of the book cannot be empty.")
    private String bookLanguage;

    @Min(value = 0, message = "Items cannot be negative")
    private Integer bookItems;

    @Size(min = 1, message = "Book genre cannot be empty.")
    private String bookGenre;
}
