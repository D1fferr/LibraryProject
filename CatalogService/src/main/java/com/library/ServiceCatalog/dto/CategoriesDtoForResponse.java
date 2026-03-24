package com.library.ServiceCatalog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoriesDtoForResponse {
    private List<CategoriesDTO> categories;
}
