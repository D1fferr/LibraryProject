package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookDto {
    private List<Book> books;
    private long bookCount;
    private int bookPage;
}
