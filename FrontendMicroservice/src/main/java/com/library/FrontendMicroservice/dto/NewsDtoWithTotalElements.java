package com.library.FrontendMicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NewsDtoWithTotalElements {
    private List<NewsDTOForGetRequest> news;
    private long newsCount;
    private int newsPages;
}