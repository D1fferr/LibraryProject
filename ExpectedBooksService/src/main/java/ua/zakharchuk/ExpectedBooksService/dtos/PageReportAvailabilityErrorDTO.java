package ua.zakharchuk.ExpectedBooksService.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class PageReportAvailabilityErrorDTO {
    private List<ReportAvailabilityErrorDTO> dtoList;
    private int totalPages;
    private int totalElements;
}
