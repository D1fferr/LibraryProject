package com.library.ServiceCatalog.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class BookDTOForResponseGetBook {
    private String bookName;

    private String bookAuthor;

    private int bookYear;

    private String bookPublication;

    private String bookLanguage;

    private int bookPieces;

    private String bookGenre;

}
