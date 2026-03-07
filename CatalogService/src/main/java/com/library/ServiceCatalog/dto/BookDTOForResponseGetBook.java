package com.library.ServiceCatalog.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data

public class BookDTOForResponseGetBook {
    private UUID bookId;

    private String bookName;

    private String bookAuthor;

    private int bookYear;

    private String bookPublication;

    private String bookLanguage;

    private int bookItems;

    private String bookGenre;


}
