package com.library.FrontendMicroservice.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
public class ExpectedBook{
    private UUID expectedBookId;
    @NotEmpty(message = "Book name must not be empty.")
    private String expectedBookName;

    @NotEmpty(message = "Author name must not be empty.")
    private String expectedBookAuthor;

    @Min(value = 1000, message = "The year of publication of the book must be greater than 1000.")
    private int expectedBookYear;

    @NotEmpty(message = "The Publication must not be empty.")
    private String expectedBookPublication;

    @NotEmpty(message = "The language of the book cannot be empty.")
    private String expectedBookLanguage;
    private String expectedBookImage;

    private int expectedBookItems;

    @NotEmpty(message = "Book genre cannot be empty.")
    private String expectedBookGenre;


}
