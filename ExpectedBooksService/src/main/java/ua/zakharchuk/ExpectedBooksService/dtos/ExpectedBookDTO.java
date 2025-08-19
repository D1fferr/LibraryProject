package ua.zakharchuk.ExpectedBooksService.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedBookDTO {
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
    private String expectedBookImage;
    @Size(min = 1, message = "Book genre cannot be empty.")
    private String expectedBookGenre;
}
