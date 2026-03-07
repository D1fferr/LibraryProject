package com.library.ServiceCatalog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookDTOForResponseCreate {
    private UUID bookId;
    private String bookName;
    private String bookAuthor;
    private int bookYear;
    private String bookPublication;
    private String bookLanguage;
    private int bookItems;
    private String bookImage;
    private String bookGenre;
}
