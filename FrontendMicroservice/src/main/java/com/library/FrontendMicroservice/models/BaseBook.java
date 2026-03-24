package com.library.FrontendMicroservice.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseBook {

    @NotEmpty(message = "Book name must not be empty.")
    private String bookName;

    @NotEmpty(message = "Author name must not be empty.")
    private String bookAuthor;

    @Min(value = 1000, message = "The year of publication of the book must be greater than 1000.")
    private int bookYear;

    @NotEmpty(message = "The Publication must not be empty.")
    private String bookPublication;

    @NotEmpty(message = "The language of the book cannot be empty.")
    private String bookLanguage;
    private String bookImage;
    private int bookItems;

    @NotEmpty(message = "Book genre cannot be empty.")
    private String bookGenre;

    @Override
    public String toString() {
        return "BaseBook{" +
                "bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookYear=" + bookYear +
                ", bookPublication='" + bookPublication + '\'' +
                ", bookLanguage='" + bookLanguage + '\'' +
                ", bookImage='" + bookImage + '\'' +
                ", bookItems=" + bookItems +
                ", bookGenre='" + bookGenre + '\'' +
                '}';
    }
}
