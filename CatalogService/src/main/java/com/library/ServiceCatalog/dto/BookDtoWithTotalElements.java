package com.library.ServiceCatalog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookDtoWithTotalElements {
    private List<BookDTOForResponseGetBook> bookDTOForResponseGetBookList;
    private long bookCount;
    private int bookPages;
}
