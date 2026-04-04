package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.ExpectedBook;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExpectedBookDtoWithTotalElements {
    private List<ExpectedBook> expectedBooks;
    private long bookCount;
    private int bookPage;
}
