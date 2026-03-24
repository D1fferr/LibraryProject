package com.library.FrontendMicroservice.dto;

import com.library.FrontendMicroservice.models.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoriesDto {
    private List<Category> categories;
}
