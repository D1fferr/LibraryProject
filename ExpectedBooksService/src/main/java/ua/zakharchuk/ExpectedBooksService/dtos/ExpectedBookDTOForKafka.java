package ua.zakharchuk.ExpectedBooksService.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedBookDTOForKafka {
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
    private int expectedBookPieces;
    private String expectedBookImage;
    @NotEmpty(message = "Book genre cannot be empty.")
    private String expectedBookGenre;
}
