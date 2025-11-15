package ua.zakharchuk.ExpectedBooksService.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpectedBookDTOCreate {
    @Size(min = 1, message = "Book name must not be empty.")
    private String expectedBookName;
    @Size(min = 1, message = "Author name must not be empty.")
    private String expectedBookAuthor;
    private int expectedBookYear;
    @Size(min = 1, message = "The Publication must not be empty.")
    private String expectedBookPublication;
    @Size(min = 1, message = "The language of the book cannot be empty.")
    private String expectedBookLanguage;
    private int expectedBookPieces;
    @Size(min = 1, message = "Book genre cannot be empty.")
    private String expectedBookGenre;
}
