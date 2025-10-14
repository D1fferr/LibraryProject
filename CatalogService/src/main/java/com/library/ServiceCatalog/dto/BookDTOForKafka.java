package com.library.ServiceCatalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTOForKafka {

    @JsonProperty("expectedBookId")
    @NotEmpty
    private UUID bookId;
    @JsonProperty("expectedBookName")
    @NotEmpty(message = "Book name must not be empty.")
    private String bookName;
    @JsonProperty("expectedBookAuthor")
    @NotEmpty(message = "Author name must not be empty.")
    private String bookAuthor;
    @JsonProperty("expectedBookYear")
    @Min(value = 1000, message = "The year of publication of the book must be greater than 1000.")
    private int bookYear;
    @JsonProperty("expectedBookPublication")
    @NotEmpty(message = "The Publication must not be empty.")
    private String bookPublication;
    @JsonProperty("expectedBookLanguage")
    @NotEmpty(message = "The language of the book cannot be empty.")
    private String bookLanguage;
    @JsonProperty("expectedBookPieces")
    private int bookPieces;
    @JsonProperty("expectedBookImage")
    private String bookImage;
    @JsonProperty("expectedBookGenre")
    @NotEmpty(message = "Book genre cannot be empty.")
    private String bookGenre;

}
