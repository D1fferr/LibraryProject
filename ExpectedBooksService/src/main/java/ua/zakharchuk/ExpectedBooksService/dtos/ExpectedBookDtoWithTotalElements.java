package ua.zakharchuk.ExpectedBooksService.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExpectedBookDtoWithTotalElements {
    private List<ExpectedBookDTO> expectedBooks;
    private long bookCount;
    private int bookPage;
}
